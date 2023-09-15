
#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "ShaderBoost/glsl/PBR.glsllib"

uniform sampler2D m_DiffuseMap;
uniform vec4 m_MainColor;
uniform vec4 m_SecondaryColor;
uniform vec4 m_MainPlaceholder;
uniform vec4 m_SecondaryPlaceholder;
uniform float m_Similarity;
uniform float m_TreadOffset1;
uniform float m_TreadOffset2;
uniform float m_TreadCoord1;
uniform float m_TreadCoord2;

#ifdef SYNCH_FADE
    uniform vec3 m_FadeOrigin;
    uniform vec3 m_FadeAxis;
    uniform vec3 m_FadeStart;
    uniform vec3 m_FadeLength;
#endif

varying vec3 wPosition;
varying vec3 wNormal;
varying vec2 texCoord;

void main() {
    
    vec2 uv = texCoord.xy;
    
    // tred animation
    if (uv.x < m_TreadCoord1) {
        uv.y += m_TreadOffset1;
    }
    else if (uv.x < m_TreadCoord2) {
        uv.y += m_TreadOffset2;
    }
    uv.y = fract(uv.y);
    
    // diffuse
    vec4 color = texture2D(m_DiffuseMap, uv);
    vec4 offsetMain = m_MainPlaceholder - color;
    vec4 offsetSec = m_SecondaryPlaceholder - color;
    if (length(offsetMain) < m_Similarity) {
        color = m_MainColor + offsetMain;
    }
    else if (length(offsetSec) < m_Similarity) {
        color = m_SecondaryColor + offsetSec;
    }
    
    // specular
    vec4 spec = vec4(0.5);
    spec.a = 1.0;
    
    // pbr
    vec4 pbr = physicallyBasedRender(wPosition, color, 1.0, spec, 0.0, wNormal);
    
    // synthetic fading
    #ifdef SYNTH_FADE
        float fadeFactor = dot(m_FadeAxis, wPosition - m_FadeOrigin);
        float synth = smoothstep(m_FadeStart-m_FadeLength, m_FadeStart, abs(fadeFactor));    
        if (fadeFactor >= 0.0) {
            synth = 1.0-synth;
        }
        pbr.a *= synth;
    #endif
    
    gl_FragColor = pbr;
    
}
