
package rpc.performer;

import com.midfield_system.api.util.Log;
import com.midfield_system.rpc.api.client.MfsRemote;
import com.midfield_system.rpc.api.client.RemoteControlException;
import com.midfield_system.rpc.api.client.StreamPerformer;

import util.ConsolePrinter;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2023.09.20
 *
 */
public class NetworkToRenderer
{
    public static void main(String[] args)
    {
        // MidField System �̃��O�o�͐���R���\�[���ɐݒ肷��D
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsRemote       mfsRmt = null;
        StreamPerformer pfmr   = null;
        
        try {
            // �R�}���h���C����������T�[�o��IP�A�h���X�ƃ|�[�g�ԍ��C
            // �y�ё��M����IP�A�h���X���擾����D
            var serverAddr = args[0];
            int serverPort = Integer.parseInt(args[1]);
            var senderAddr = args[2];
            
            // ���T�[�o�Ɛڑ����C���u������J�n����D
            mfsRmt = new MfsRemote(serverAddr, serverPort, err -> System.err.println(err));
            mfsRmt.initializeRemoteControl();
            
            // ����M�X�g���[���œ��͂��\������D
            // �E���M�������M���Ă���X�g���[����I�����Đݒ肷��D
            var stmInfMgr = mfsRmt.getStreamInfoManager();
            var lsStmInf  = stmInfMgr.fetchSourceStreamInfoList(senderAddr);
            if (lsStmInf.size() <= 0) {
                System.out.println("����M�\�ȃX�g���[��������܂���D");
                return;
            }
            // RcSegmentIo �̓��͂���M�X�g���[���Ƃ��č\������D
            var segIo = mfsRmt.newSegmentIo();
            segIo.configureIncomingStream(lsStmInf.get(0));
            
            // �����������_���ŏo�͂��\������D
            segIo.configurePreferredRenderer();
            
            // ��RcStreamPerformer �𐶐�����D
            pfmr = mfsRmt.newStreamPerformer(segIo);
            
            // ��RcStreamPerformer �̓��o�͏������J�n����D
            pfmr.start();
            
            // ��RcStreamPerformer �̓��o�͏������I������D
            System.out.print("> Enter �L�[�̓��͂�҂��܂��D");
            System.in.read();
            
            pfmr.stop();
        }
        catch (RemoteControlException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // ��RcStreamPerformer �̑S�Ă̏������I������D
            if (pfmr != null) {
                pfmr.delete();
            }
            // ���T�[�o�Ɛؒf���C���u������I������D
            if (mfsRmt != null) {
                mfsRmt.shutdownRemoteControl();
            }
        }
    }
}