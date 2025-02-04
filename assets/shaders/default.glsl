#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexCoords;
layout (location = 3) in float aTexId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;

void main()
{
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;
}
#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];

out vec4 Color;

void main()
{
    if(fTexId > 0){
        int id = int(fTexId);
        Color = fColor * texture(uTextures[id], fTexCoords);
        //Color = vec4(fTexCoords, 0.0, 1.0);
    } else{
        Color = fColor;
    }
    //float noise = fract(sin(dot(fColor.xy, vec2(12.9898, 78.233))) * 43758.5453);

}