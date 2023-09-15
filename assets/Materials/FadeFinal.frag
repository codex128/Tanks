
#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/MultiSample.glsllib"

// input from the filter
uniform COLORTEXTURE m_Texture;

// input from SyntheticFade
uniform sampler2D m_AlphaMap;

varying vec2 texCoord;

void main() {
    
    vec4 color = getColor(m_Texture, texCoord);
    vec4 alpha = texture2D(m_AlphaMap, texCoord);
    
    gl_FragColor = vec4(color.rgb, color.a*alpha.a);
    
}
