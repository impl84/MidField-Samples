
package stream;

import java.io.IOException;
import java.util.List;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.DeviceInfo;
import com.midfield_system.api.stream.DeviceInfoManager;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamFormat;
import com.midfield_system.api.stream.StreamInfoManager;
import com.midfield_system.api.util.LogPrinter;
import com.midfield_system.protocol.StreamInfo;

import util.LineReader;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: ConfigTool
 *
 * Date Modified: 2021.11.05
 *
 */

//==============================================================================
class ConfigTool
{
//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================
	
	//- PRIVATE VARIABLE -------------------------------------------------------
	
	// �s�P�ʂ̕�������o�͗p�C���^�[�t�F�[�X
	private final LineReader reader;
	private final LogPrinter printer;
	
//==============================================================================
//  INSTANCE METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	ConfigTool(LineReader reader, LogPrinter printer)
	{
		this.reader = reader;
		this.printer = printer;
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	// SegmentIo �̓���(�f�o�C�X)���\������D
	//
	void configureInputDevice(SegmentIo segIo)
		throws	IOException	
	{
		// �r�f�I�ƃI�[�f�B�I�̓��̓f�o�C�X��񃊃X�g���擾����D
		DeviceInfoManager devInfMgr = DeviceInfoManager.getInstance();
		List<DeviceInfo> lsVidDev = devInfMgr.getInputVideoDeviceInfoList();
		List<DeviceInfo> lsAudDev = devInfMgr.getInputAudioDeviceInfoList();
		
		// �r�f�I�ƃI�[�f�B�I�̓��̓f�o�C�X����I������D
		// �i�����ł͍ŏ��̗v�f��I������D�j
		DeviceInfo vidDev = lsVidDev.get(0);
		DeviceInfo audDev = lsAudDev.get(0);
		
		// ���̓f�o�C�X�̏o�̓t�H�[�}�b�g�����߂�D
		// �i�����ł̓f�t�H���g�̃t�H�[�}�b�g�𗘗p����D�j
		StreamFormat vidFmt = vidDev.getDefaultOutputFormat();
		StreamFormat audFmt = audDev.getDefaultOutputFormat();
		
		// Segment I/O �̓��͂���̓f�o�C�X���Əo�̓t�H�[�}�b�g�ō\������D
		segIo.configureInputDevice(vidDev, vidFmt, audDev, audFmt);
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	// SegmentIo �̓���(��M�X�g���[��)���\������D
	//
	void configureIncomingStream(SegmentIo segIo)
		throws	IOException	
	{
		// ���M�z�X�g��/IP�A�h���X���R�}���h���C������擾����D
		this.printer.printf("  ���M�z�X�g��/IP�A�h���X�F");
		String srcAddr = this.reader.readLine();
			// IOException
		
		// �X�g���[����񃊃X�g�𑗐M�z�X�g����擾����D
		StreamInfoManager stmInfMgr = StreamInfoManager.getInstance();
		List<StreamInfo> lsStmInf = stmInfMgr.fetchSourceStreamInfoList(srcAddr);
		if (lsStmInf.size() <= 0) {
			throw new IOException("  ����M�\�ȃX�g���[��������܂���D");
		}
		// ���M�z�X�g�̏o�̓X�g���[������I���i�����ł͍ŏ��̗v�f��I���j���C
		// SegmentIo �̓��͂Ƃ��č\������D
		StreamInfo stmInf = lsStmInf.get(0);
		segIo.configureIncomingStream(stmInf);		
	}
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD: SegmentIo �̏o�͂��\�����郁�\�b�h
//------------------------------------------------------------------------------
	
	//- PACKAGE METHOD ---------------------------------------------------------
	// SegmentIo �̏o��(�����_��)���\������D
	//
	void configureDefaultRenderer(SegmentIo segIo)
		throws	IOException	
	{
		// �o�͂��f�t�H���g�����_���Ƃ��č\������D
		segIo.configureDefaultRenderer();
		
		// ���C�u�\�[�X�I�v�V������L���ɂ���D
		segIo.setLiveSource(true);
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	// SegmentIo �̏o��(���M�X�g���[��)���\������D
	//
	void configureOutgoingStream(SegmentIo segIo)
		throws	IOException	
	{
		// �o�͉\�ȃt�H�[�}�b�g��񃊃X�g���擾����D
		List<StreamFormat> lsOutVidFmt = segIo.getOutputVideoFormatList();
		List<StreamFormat> lsOutAudFmt = segIo.getOutputAudioFormatList();
		
		// �o�̓t�H�[�}�b�g�����擾����D�i�����ł͍ŏ��̗v�f��I���j
		StreamFormat vidFmt = lsOutVidFmt.get(0);
		StreamFormat audFmt = lsOutAudFmt.get(0);
		
		// �I�������r�f�I�t�H�[�}�b�g�ƃv���g�R�����o�͂Ƃ��č\������D
		segIo.configureOutgoingStream(vidFmt, audFmt);
		
		// �g�����X�|�[�g�v���g�R���̐ݒ���s���D
		segIo.setTransportProtocol(
			ProtocolType.TCP,		// TCP�𗘗p����D
			ConnectionMode.PASSIVE	// �R�l�N�V�����ڑ��v�����󂯓����D
		);
		// �v���r���[���\�𗘗p���邽�߂̃I�v�V������L���ɂ���D
		segIo.setPreviewer();
		
		// ���C�u�\�[�X�I�v�V������L���ɂ���D
		segIo.setLiveSource(true);		
	}
}
