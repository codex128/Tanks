
#ifdef GLOW_IMAGE
    uniform sampler2D m_GlowMap;
    varying vec2 texCoord;
#endif
#ifdef GLOW_COLOR
    uniform vec4 m_GlowColor;
#endif

void main() {
    
    #ifdef GLOW_COLOR
        vec4 color = m_GlowColor;
    #else
        vec4 color = vec4(1.0);
    #endif
    
    #ifdef GLOW_IMAGE
        gl_FragColor = texture2D(m_GlowMap, texCoord) * color;
    #else
        gl_FragColor = color;
    #endif
    
}
