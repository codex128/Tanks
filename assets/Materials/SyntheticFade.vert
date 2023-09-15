#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/Skinning.glsllib"
#import "Common/ShaderLib/Instancing.glsllib"
#import "Common/ShaderLib/MorphAnim.glsllib"

attribute vec3 inPosition;
attribute vec4 inColor;

varying vec3 wPosition;
#ifdef HAS_POINTSIZE
    uniform float m_PointSize;
#endif

void main() {
    
    wPosition = inPosition;

    #ifdef HAS_POINTSIZE
        gl_PointSize = m_PointSize;
    #endif

    vec4 modelSpacePos = vec4(inPosition, 1.0);

    #ifdef NUM_MORPH_TARGETS
        Morph_Compute(modelSpacePos);
    #endif

    #ifdef NUM_BONES
        Skinning_Compute(modelSpacePos);
    #endif

    gl_Position = TransformWorldViewProjection(modelSpacePos);
}