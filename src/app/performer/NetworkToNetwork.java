
package app.performer;

import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamInfoManager;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsException;
import com.midfield_system.midfield.MfsApplication;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2023.09.13
 *
 */
public class NetworkToNetwork
{
    public static void main(String[] args)
    {
        StreamPerformer pfmr = null;
        
        try {
            // �R�}���h���C���������瑗�M����IP�A�h���X���擾����D
            var senderAddr = args[0];
            
            // ��MidField System �̃A�v���P�[�V�������N������D
            var mfsApp = MfsApplication.launch();
            
            // ����M�X�g���[���œ��͂��\������D
            // �E���M�������M���Ă���X�g���[����I�����Đݒ肷��D
            var stmInfMgr = StreamInfoManager.getInstance();
            var lsStmInf  = stmInfMgr.fetchSourceStreamInfoList(senderAddr);
            if (lsStmInf.size() <= 0) {
                System.out.println("����M�\�ȃX�g���[��������܂���D");
                return;
            }
            var segIo = new SegmentIo();
            segIo.configureIncomingStream(lsStmInf.get(0));
            
            // �����M�X�g���[���ŏo�͂��\������D
            // �E���M�t�H�[�}�b�g��I�����Đݒ肷��D
            // �ETCP�𗘗p���C�R�l�N�V�����ڑ��v�����󂯓����D
            // �E�����v���r���[���\�𗘗p����D
            var lsVidFmt = segIo.getOutputVideoFormatList();
            if (lsVidFmt.size() <= 0) {
                System.out.println("�����M�\�ȃr�f�I�t�H�[�}�b�g������܂���D");
                return;
            }
            var lsAudFmt = segIo.getOutputAudioFormatList();
            if (lsAudFmt.size() <= 0) {
                System.out.println("�����M�\�ȃI�[�f�B�I�t�H�[�}�b�g������܂���D");
                return;
            }
            segIo.configureOutgoingStream(lsVidFmt.get(0), lsAudFmt.get(0));
            segIo.setTransportProtocol(ProtocolType.TCP, ConnectionMode.PASSIVE);
            segIo.setPreferredPreviewer();
            
            // ��StreamPerformer �𐶐����CUI�֒ǉ�����D
            pfmr = StreamPerformer.newInstance(segIo);
            mfsApp.addStreamPerformer(pfmr);
            
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
        }
    }
}