
package rc.performer;

import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_CODE;
import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_DATA;
import static com.midfield_system.api.rpc.JsonRpcConstants.ERR_MESSAGE;

import java.util.Map;

import com.midfield_system.api.rpc.ErrorResponseHandler;
import com.midfield_system.api.stream.ConnectionMode;
import com.midfield_system.api.stream.ProtocolType;
import com.midfield_system.api.util.Log;

import rc.api.RcDeviceInfo;
import rc.api.RcDeviceInfoManager;
import rc.api.RcMfsNode;
import rc.api.RcSegmentIo;
import rc.api.RcStreamPerformer;
import util.ConsolePrinter;
import util.ConsoleReader;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RcDeviceToNetwork
 * 
 * Date Modified: 2022.10.17
 *
 */
public class RcDeviceToNetwork
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final String SERVER_ADDR = "172.16.127.206";
    private static final int    SERVER_PORT = 60202;
    
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
            
            // �r�f�I�ƃI�[�f�B�I�̓��̓f�o�C�X��񃊃X�g���擾���C
            // ���p������̓f�o�C�X��I������D
            // �i�����ł͍ŏ��̓��̓f�o�C�X��I������D�j
            RcDeviceInfoManager devInfMgr = mfs.getRcDeviceInfoManager();
            RcDeviceInfo        vidDev    = devInfMgr.getInputVideoDeviceInfoList().get(0);
            RcDeviceInfo        audDev    = devInfMgr.getInputAudioDeviceInfoList().get(0);
            
            // RcSegmentIo �̓��͂���̓f�o�C�X�Ƃ��č\������D
            RcSegmentIo segIo = mfs.newRcSegmentIo();
            segIo.configureInputDevice(vidDev, audDev);
            
            // RcSegmentIo �̏o�͂𑗐M�X�g���[���Ƃ��č\������D
            segIo.configureOutgoingStream(
                segIo.getOutputVideoFormatList().get(0),
                segIo.getOutputAudioFormatList().get(0)
            );
            // �g�����X�|�[�g�v���g�R���̐ݒ���s���D
            segIo.setTransportProtocol(
                ProtocolType.TCP,       // TCP�𗘗p����D
                ConnectionMode.PASSIVE  // �R�l�N�V�����ڑ��v�����󂯓����D
            );
            // �I�v�V�����̐ݒ������D
            segIo.setPreviewer();       // �v���r���[���\�𗘗p����D
            segIo.setLiveSource(true);  // ���C�u�\�[�X�I�v�V������L���ɂ���D
            
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
