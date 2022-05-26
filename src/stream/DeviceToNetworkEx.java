
package stream;

import java.io.IOException;

import com.midfield_system.api.stream.SegmentIo;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: DeviceToNetworkEx
 *
 * Date Modified: 2021.09.19
 *
 */
class DeviceToNetworkEx
    extends
        AbstractSampleCode
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String DESCRIPTION = "Device -> Outgoing Stream";
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    DeviceToNetworkEx()
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
        // SegentIo �̏o�͂𑗐M�X�g���[���Ƃ��č\������D
        cfgTool.configureOutgoingStream(segIo);
        // IOException
    }
}