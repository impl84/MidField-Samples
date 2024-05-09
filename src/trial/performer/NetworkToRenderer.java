
package trial.performer;

import java.util.List;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.SegmentIo;
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
public class NetworkToRenderer
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
            
            // ���o�͂������_���Ƃ��č\������D
            segIo.configureRenderer();
            
            // ��StreamPerformer �𐶐����C���̓����ɂ��� VideoCanvas �����o���D
            //   ���̌�CVideoCanvas ��\�����邽�߂� SimpleViewer �𐶐����ĕ\������D
            pfmr = StreamPerformer.newInstance(segIo);
            VideoCanvas vidCvs = pfmr.getVideoCanvas();
            SwingUtilities.invokeAndWait(() -> {
                new SimpleViewer("Network to Renderer", vidCvs);
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