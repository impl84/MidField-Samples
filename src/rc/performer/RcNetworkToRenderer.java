
package rc.performer;

import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_CODE;
import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_DATA;
import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_MESSAGE;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.midfield_system.api.rpc.ErrorResponseHandler;
import com.midfield_system.api.util.Log;

import rc.RcMfsNode;
import rc.RcSegmentIo;
import rc.RcStreamInfo;
import rc.RcStreamInfoManager;
import rc.RcStreamPerformer;
import util.ConsolePrinter;
import util.ConsoleReader;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcNetworkToRenderer
 * 
 * Date Modified: 2022.06.09
 *
 */
public class RcNetworkToRenderer
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String SERVER_ADDR = "172.16.126.156";
    private static final int    SERVER_PORT = 60202;
    
    private static final String SENDER_ADDR = "172.16.126.155";
    
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
        
        RcMfsNode         mfs  = null;
        RcStreamPerformer pfmr = null;
        
        try {
            // RcMfsNode �𐶐����C���u������J�n����D
            mfs = new RcMfsNode(SERVER_ADDR, SERVER_PORT, HANDLER);
            mfs.open();
            
            // �X�g���[����񃊃X�g�𑗐M�z�X�g����擾����D
            RcStreamInfoManager stmInfMgr = mfs.getRcStreamInfoManager();
            List<RcStreamInfo>  lsStmInf  = stmInfMgr.fetchSourceStreamInfoList(SENDER_ADDR);
            if (lsStmInf.size() <= 0) {
                throw new IOException("  ����M�\�ȃX�g���[��������܂���D");
            }
            // RcSegmentIo �̓��͂���M�X�g���[���Ƃ��č\������D
            RcSegmentIo segIo = mfs.newRcSegmentIo();
            segIo.configureIncomingStream(lsStmInf.get(0));
            
            // RcSegmentIo �̏o�͂��f�t�H���g�����_���Ƃ��č\������D
            segIo.configureDefaultRenderer();
            
            // ���C�u�\�[�X�I�v�V������L���ɂ���D
            segIo.setLiveSource(true);
            
            // RcSegmentIo �����Ƃ� RcStreamPerformer �𐶐�����D
            pfmr = mfs.newRcStreamPerformer(segIo);
            
            // ���o�͏������J�n����D
            pfmr.open();
            pfmr.start();
            
            // Enter�L�[�̓��͂�҂D
            System.out.printf("> Enter �L�[�̓��͂�҂��܂��D");
            reader.readLine();
            
            // ���o�͏������I������D
            pfmr.stop();
            pfmr.close();
        }
        catch (Exception ex) {
            // ���u���쒆�ɗ�O�����������D
            ex.printStackTrace();
        }
        finally {
            // RcStreamPerformer �̏������I������D
            if (pfmr != null) {
                try {
                    pfmr.delete();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            // RcMfsNode �̏������I������D
            if (mfs != null) {
                mfs.close();
            }
            // ConsoleReader ���������D
            reader.release();
        }
    }
    
// -----------------------------------------------------------------------------
// PRIVATE STATIC LAMBDA EXPRESSION:
// -----------------------------------------------------------------------------
    
    // - PRIVATE STATIC LAMBDA EXPRESSION --------------------------------------
    //    
    private static final ErrorResponseHandler HANDLER = (reason, response, ex) -> {
        // RPC�����������ɔ��������G���[�����o�͂���D
        if (response != null) {
            Map<String, Object> error = response.getError();
            System.out.printf(
                "error: code: %s, message: %s, data: %s",
                error.get(ERR_CODE),
                error.get(ERR_MESSAGE),
                error.get(ERR_DATA)
            );
        }
        if (ex != null) {
            ex.printStackTrace();
        }
    };
}
