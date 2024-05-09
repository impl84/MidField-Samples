
package trial.performer;

import java.util.List;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamFormat;
import com.midfield_system.api.stream.StreamInfoManager;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsException;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.util.Log;
import com.midfield_system.api.viewer.VideoCanvas;
import com.midfield_system.protocol.StreamInfo;

import util.ConsolePrinter;
import util.SimpleViewer;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.08.28
 *
 */
public class NetworkToNetwork
{
    public static void main(String[] args)
    {
        // MidField System �̃��O�o�͐���R���\�[���ɐݒ肷��D
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsNode         mfs  = null;
        StreamPerformer pfmr = null;
        
        try {
            // �R�}���h���C���������瑗�M����IP�A�h���X���擾����D
            String senderAddr = args[0];
            
            // ��MidField System �����������C�N������D
            mfs = MfsNode.initialize();
            mfs.activate();
            
            // ����M�X�g���[���œ��͂��\������D
            // �E���M�������M���Ă���X�g���[����I�����Đݒ肷��D
            StreamInfoManager stmInfMgr = StreamInfoManager.getInstance();
            List<StreamInfo>  lsStmInf  = stmInfMgr.fetchSourceStreamInfoList(senderAddr);
            if (lsStmInf.size() <= 0) {
                System.out.println("����M�\�ȃX�g���[��������܂���D");
                return;
            }
            SegmentIo segIo = new SegmentIo();
            segIo.configureIncomingStream(lsStmInf.get(0));
            
            // �����M�X�g���[���ŏo�͂��\������D
            // �E���M�t�H�[�}�b�g��I�����Đݒ肷��D
            // �ETCP�𗘗p���C�R�l�N�V�����ڑ��v�����󂯓����D
            // �E�v���r���[���\�𗘗p����D
            List<StreamFormat> lsVidFmt = segIo.getOutputVideoFormatList();
            if (lsVidFmt.size() <= 0) {
                System.out.println("�����M�\�ȃr�f�I�t�H�[�}�b�g������܂���D");
                return;
            }
            List<StreamFormat> lsAudFmt = segIo.getOutputAudioFormatList();
            if (lsAudFmt.size() <= 0) {
                System.out.println("�����M�\�ȃI�[�f�B�I�t�H�[�}�b�g������܂���D");
                return;
            }
            segIo.configureOutgoingStream(lsVidFmt.get(0), lsAudFmt.get(0));
            segIo.setTransportProtocol(ProtocolType.TCP, ConnectionMode.PASSIVE);
            segIo.setPreviewer();
            
            // ��StreamPerformer �𐶐����C���̓����ɂ��� VideoCanvas �����o���D
            //   ���̌�CVideoCanvas ��\�����邽�߂� SimpleViewer �𐶐����ĕ\������D
            pfmr = StreamPerformer.newInstance(segIo);
            VideoCanvas vidCvs = pfmr.getVideoCanvas();
            SwingUtilities.invokeAndWait(() -> {
                new SimpleViewer("Network to Network", vidCvs);
            });
            // ��StreamPerformer �̓��o�͏������J�n����D
            pfmr.open();
            pfmr.start();
            
            // ��StreamPerformer �̓��o�͏������I������D
            System.out.print("> Enter �L�[�̓��͂�҂��܂��D");
            System.in.read();
            
            pfmr.stop();
            pfmr.close();
        }
        catch (MfsException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ��StreamPerformer �̑S�Ă̏������I������D
            if (pfmr != null) {
                pfmr.delete();
            }
            // ��MidField System ���I������D
            if (mfs != null) {
                mfs.shutdown();
            }
        }
    }
}