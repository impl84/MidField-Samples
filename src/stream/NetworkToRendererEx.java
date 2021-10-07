
package stream;

import java.io.IOException;

import com.midfield_system.api.stream.SegmentIo;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: NetworkToRendererEx
 *
 * Date Modified: 2021.09.19
 *
 */

//==============================================================================
class NetworkToRendererEx
	extends	AbstractSampleCode
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String
		DESCRIPTION	= "Incoming Stream -> Renderer";
	
//==============================================================================
//  INSTANCE METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	NetworkToRendererEx()
	{
		//
	}
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD: OVERRIDES: AbstractSampleCode
//------------------------------------------------------------------------------
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//- OVERRIDES: AbstractSampleCode
	//
	@Override
	String getDescription()
	{
		return	DESCRIPTION;
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//- OVERRIDES: AbstractSampleCode
	//
	@Override
	void configureInput(ConfigTool cfgTool, SegmentIo segIo)
		throws	IOException
	{
		// SegmentIo �̓��͂���M�X�g���[���Ƃ��č\������D
		cfgTool.configureIncomingStream(segIo);
			// IOException
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//- OVERRIDES: AbstractSampleCode
	//
	@Override
	void configureOutput(ConfigTool cfgTool, SegmentIo segIo)
		throws	IOException
	{
		// SegmentIo �̏o�͂��f�t�H���g�����_���Ƃ��č\������D
		cfgTool.configureDefaultRenderer(segIo);
			// IOException
	}
}