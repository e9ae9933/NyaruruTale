#version 330 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 tpos;
uniform sampler2D ourTexture;
out vec4 theColor;

void main()
{
    gl_Position = vec4(tpos.x, tpos.y, 0.0, 1.0);
    theColor=vec4(0.5f,0.3f,0.2f,1.0f);
//     theColor=texture(ourTexture,vec2(tpos.x,tpos.y));
}