
package stream;

import java.io.IOException;
import java.util.List;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamInfoManager;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.viewer.VideoCanvas;
import com.midfield_system.protocol.StreamInfo;

import util.ConsoleReader;
import util.SimpleViewer;

// Sample code of MidField System API
// Date Modified: 2021.09.19
//
public class NetworkToNetwork
{
	public static void main(String[] args)
	{
		MfsNode mfs = null;
		StreamPerformer pfmr = null;
		ConsoleReader reader = ConsoleReader.getInstance();
		
		try {
			// MidField System �����������C�N������D
			mfs = MfsNode.initialize();		// SystemException
			mfs.activate();					// SystemException
			
			// ���M�z�X�g��/IP�A�h���X���R�}���h���C������擾����D
			System.out.print("  ���M�z�X�g��/IP�A�h���X�F");
			String srcAddr = reader.readLine();	// IOException
			
			// �X�g���[����񃊃X�g�𑗐M�z�X�g����擾����D
			StreamInfoManager stmInfMgr = StreamInfoManager.getInstance();
			List<StreamInfo> lsStmInf = stmInfMgr.fetchSourceStreamInfoList(srcAddr);
			if (lsStmInf.size() <= 0) {
				throw new IOException("  ����M�\�ȃX�g���[��������܂���D");
			}
			// SegmentIo �̓��͂���M�X�g���[���Ƃ��č\������D
			SegmentIo segIo = new SegmentIo();
			segIo.configureIncomingStream(lsStmInf.get(0));
			
			// SegmentIo �̏o�͂𑗐M�X�g���[���Ƃ��č\�����C
			// �g�����X�|�[�g�v���g�R���̐ݒ���s���D
			segIo.configureOutgoingStream(
				segIo.getOutputVideoFormatList().get(0),
				segIo.getOutputAudioFormatList().get(0)
			);
			segIo.setTransportProtocol(
				ProtocolType.TCP,		// TCP�𗘗p����D
				ConnectionMode.PASSIVE	// �R�l�N�V�����ڑ��v�����󂯓����D
			);
			// �I�v�V�����̐ݒ������D
			segIo.setPreviewer();		// �v���r���[���\�𗘗p����D
			segIo.setLiveSource(true);	// ���C�u�\�[�X�I�v�V������L���ɂ���D	
			
			// SegmentIo �����Ƃ� StreamPerformer �𐶐�����D
			pfmr = StreamPerformer.newInstance(segIo);	// SystemException, StreamException
			
			// StreamPerformer ���� VideoCanvas ���擾����D
			VideoCanvas vidCvs = pfmr.getVideoCanvas();
			
			// StreamPerformer ���� VideoCanvas ���擾���CSimpleViewer �ɒǉ�����D
			SwingUtilities.invokeAndWait(() -> {
				new SimpleViewer("Network to Network", vidCvs);
			});
				// InterruptedException, InvocationTargetException
			
			// ���o�͏������J�n����D
			pfmr.open();	// StreamException
			pfmr.start();	// StreamException
			
			// Enter�L�[�̓��͂�҂D
			System.out.print("> Enter �L�[�̓��͂�҂��܂��D");
			reader.readLine();	// IOException
			
			// ���o�͏������I������D
			pfmr.stop();	// StreamException
			pfmr.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			// StreamPerformer, MidField System ���I������D
			if (pfmr != null) { pfmr.delete(); }
			if (mfs != null) { mfs.shutdown(); }
			
			// ConsoleReader ���������D
			reader.release();
		}
	}
}