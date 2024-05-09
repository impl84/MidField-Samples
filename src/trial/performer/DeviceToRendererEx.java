
package trial.performer;

import java.io.IOException;

import com.midfield_system.api.stream.SegmentIo;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.08.28
 *
 */
class DeviceToRendererEx
    extends
        AbstractSampleCode
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String DESCRIPTION = "Device -> Renderer";
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    DeviceToRendererEx()
    {
        //
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD: OVERRIDES: AbstractSampleCode
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    // - OVERRIDES: AbstractSampleCode
    //
    @Override
    String getDescription()
    {
        return DESCRIPTION;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    // - OVERRIDES: AbstractSampleCode
    //
    @Override
    void configureInput(ConfigTool cfgTool, SegmentIo segIo)
        throws IOException
    {
        // SegmentIo �̓��͂���̓f�o�C�X�Ƃ��č\������D
        cfgTool.configureInputDevice(segIo);
        // IOException
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    // - OVERRIDES: AbstractSampleCode
    //
    @Override
    void configureOutput(ConfigTool cfgTool, SegmentIo segIo)
        throws IOException
    {
        // SegmentIo �̏o�͂������_���Ƃ��č\������D
        cfgTool.configureRenderer(segIo);
        // IOException
    }
}