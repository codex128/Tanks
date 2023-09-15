
uniform sampler2D m_ColorMap;
uniform float m_Seed;

#ifdef SYNCH_FADE
    uniform vec3 m_FadeOrigin;
    uniform vec3 m_FadeAxis;
    uniform vec3 m_FadeStart;
    uniform vec3 m_FadeLength;
#endif

varying vec3 wPosition;
varying vec2 texCoord;

void main() {
    
    float offset = fract(sin(m_Seed) * 3830.179);
    vec2 uv = fract(vec2(texCoord.x + offset, texCoord.y));
    
    vec4 color = texture2D(m_ColorMap, uv);
    if (color.a < 0.5) {
        discard;
    }
    
    // synthetic fading
    #ifdef SYNTH_FADE
        float fadeFactor = dot(m_FadeAxis, wPosition - m_FadeOrigin);
        float synth = smoothstep(m_FadeStart-m_FadeLength, m_FadeStart, abs(fadeFactor));    
        if (fadeFactor >= 0.0) {
            synth = 1.0-synth;
        }
        color.a *= synth;
    #endif
    
    gl_FragColor = color;
    
}
