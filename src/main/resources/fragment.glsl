#version 330

in  vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 color;
    // Light position is assumed to be in view coordinates
    vec3 position;
    float intensity;
    Attenuation att;
};

struct Material
{
    vec3 color;
    int useColor;
    float reflectance;
};

uniform sampler2D texture_sampler;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;
uniform vec3 camera_pos;

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specColor = vec4(0, 0, 0, 0);

    // Diffuse Light
    vec3 light_direction = light.position - position;
    vec3 to_light_source  = normalize(light_direction);
    float diffuseFactor = max(dot(normal, to_light_source ), 0.0);
    diffuseColor = vec4(light.color, 1.0) * light.intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(camera_pos - position);
    vec3 from_light_source = -to_light_source;
    vec3 reflected_light = normalize(reflect(from_light_source, normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColor = specularFactor * material.reflectance * vec4(light.color, 1.0);

    // Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance +
        light.att.exponent * distance * distance;
    return (diffuseColor + specColor) / attenuationInv;
}

void main()
{
    vec4 baseColor;
    if ( material.useColor == 1 )
    {
        baseColor = vec4(material.color, 1);
    }
    else
    {
        baseColor = texture(texture_sampler, outTexCoord);
    }
    vec4 lightColor = calcPointLight(pointLight, mvVertexPos, mvVertexNormal);

    vec4 totalLight = vec4(ambientLight, 1.0);
    totalLight += lightColor;

    fragColor = baseColor * totalLight;
    
}