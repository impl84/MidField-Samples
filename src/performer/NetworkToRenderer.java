
package performer;

import java.io.IOException;
import java.util.List;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamInfoManager;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.util.Log;
import com.midfield_system.api.viewer.VideoCanvas;
import com.midfield_system.protocol.StreamInfo;

import util.ConsolePrinter;
import util.ConsoleReader;
import util.SimpleViewer;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: NetworkToRenderer
 *
 * Date Modified: 2022.06.09
 *
 */
public class NetworkToRenderer
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String SENDER_ADDR = "172.16.126.156";
    
// =============================================================================
// CLASS METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC STATIC METHOD --------------------------------------------------
    //
    public static void main(String[] args)
    {
        // �R���\�[������̕������͂����� ConsoleReader �̃C���X�^���X���擾����D
        ConsoleReader reader = ConsoleReader.getInstance();
        
        // MidField System �̃��O�o�͐���R���\�[���ɐݒ肷��D
        Log.setLogPrinter(ConsolePrinter.getInstance());
        
        MfsNode         mfs  = null;
        StreamPerformer pfmr = null;
        
        try {
            // MidField System �����������C�N������D
            mfs = MfsNode.initialize();
            mfs.activate();
            
            // �X�g���[����񃊃X�g�𑗐M�z�X�g����擾����D
            StreamInfoManager stmInfMgr = StreamInfoManager.getInstance();
            List<StreamInfo>  lsStmInf  = stmInfMgr.fetchSourceStreamInfoList(SENDER_ADDR);
            if (lsStmInf.size() <= 0) {
                throw new IOException("  ����M�\�ȃX�g���[��������܂���D");
            }
            // SegmentIo �̓��͂���M�X�g���[���Ƃ��č\������D
            SegmentIo segIo = new SegmentIo();
            segIo.configureIncomingStream(lsStmInf.get(0));
            
            // SegmentIo �̏o�͂��f�t�H���g�����_���Ƃ��č\������D
            segIo.configureDefaultRenderer();
            
            // ���C�u�\�[�X�I�v�V������L���ɂ���D
            segIo.setLiveSource(true);
            
            // SegmentIo �����Ƃ� StreamPerformer �𐶐�����D
            pfmr = StreamPerformer.newInstance(segIo);
            
            // StreamPerformer ���� VideoCanvas ���擾����D
            VideoCanvas vidCvs = pfmr.getVideoCanvas();
            
            // StreamPerformer ���� VideoCanvas ���擾���CSimpleViewer �ɒǉ�����D
            SwingUtilities.invokeAndWait(() -> {
                new SimpleViewer("Network to Renderer", vidCvs);
            });
            // ���o�͏������J�n����D
            pfmr.open();
            pfmr.start();
            
            // Enter�L�[�̓��͂�҂D
            System.out.print("> Enter �L�[�̓��͂�҂��܂��D");
            reader.readLine();
            
            // ���o�͏������I������D
            pfmr.stop();
            pfmr.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // StreamPerformer �̏������I������D
            if (pfmr != null) {
                pfmr.delete();
            }
            // MidField System ���I������D
            if (mfs != null) {
                mfs.shutdown();
            }
            // ConsoleReader ���������D
            reader.release();
        }
    }
}