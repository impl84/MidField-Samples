
package stream;

import java.io.IOException;

import com.midfield_system.api.stream.SegmentIo;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: NetworkToNetworkEx
 *
 * Date Modified: 2021.10.26
 *
 */

//==============================================================================
class NetworkToNetworkEx
	extends	AbstractSampleCode
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String
		DESCRIPTION	= "Incoming Stream -> Outgoing Stream";
	
//==============================================================================
//  INSTANCE METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	NetworkToNetworkEx()
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
		// SegmentIo の入力を受信ストリームとして構成する．
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
		// SegentIo の出力を送信ストリームとして構成する．
		cfgTool.configureOutgoingStream(segIo);
			// IOException
	}
}