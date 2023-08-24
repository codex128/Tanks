
uniform sampler2D m_ColorMap;
uniform float m_Seed;

varying vec2 texCoord;

void main() {
    
    float offset = fract(sin(m_Seed) * 3830.179);
    vec2 uv = fract(vec2(texCoord.x + offset, texCoord.y));
    
    
    vec4 color = texture2D(m_ColorMap, uv);
    if (color.a < 0.5) {
        discard;
    }
    gl_FragColor = color;
    
}
