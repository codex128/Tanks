
#import "Common/ShaderLib/GLSLCompat.glsllib"

uniform vec3 m_FadeOrigin;
uniform vec3 m_FadeAxis;
uniform float m_FadeStart;
uniform float m_FadeLength;

varying vec3 wPosition;

void main() {
    
    // the similarity of the difference vector to the axis vector
    float factor = dot(m_FadeAxis, wPosition - m_FadeOrigin);
    float alpha = smoothstep(m_FadeStart-m_FadeLength, m_FadeStart, abs(factor));    
    if (factor >= 0.0) {
        alpha = 1.0-alpha;
    }
    gl_FragColor = vec4(0.0, 0.0, 0.0, alpha);
    
}
