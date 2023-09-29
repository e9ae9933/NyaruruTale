#version 330 core

in vec4 theColor;
out vec4 color;

uniform sampler2D ourTexture;

void main()
{
    color = theColor;
}