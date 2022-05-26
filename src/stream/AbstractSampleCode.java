
package stream;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.SwingUtilities;

import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamException;
import com.midfield_system.api.stream.StreamPerformer;
import com.midfield_system.api.viewer.VideoCanvas;

import util.SimpleViewer;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: AbstractSampleCode
 *
 * Date Modified: 2021.08.24
 *
 */
abstract class AbstractSampleCode
    implements
        WindowListener
{
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    
    // StreamPerformer
    private StreamPerformer pfmr = null;
    
    // �r�f�I��\�����邽�߂̃r���[��
    private SimpleViewer viewer = null;
    
    // �T���v���R�[�h�̓�����(true:���쒆�Cfalse:��~��)
    private boolean isRunning = false;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: IMPLEMENTS: WindowListener
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowActivated(WindowEvent ev)
    {}
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowClosed(WindowEvent ev)
    {}
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowClosing(WindowEvent ev)
    {
        // SimpleViewer ����悤�Ƃ��Ă����ԁD
        // �T���v���R�[�h�̏������I������D
        closeSampleCode();
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowDeactivated(WindowEvent ev)
    {}
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowDeiconified(WindowEvent ev)
    {}
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowIconified(WindowEvent ev)
    {}
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - IMPLEMENTS: WindowListener
    //
    @Override
    public void windowOpened(WindowEvent ev)
    {}
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    AbstractSampleCode()
    {
        //
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    // �T���v���R�[�h�̓�����(true:���쒆�Cfalse:��~��)��Ԃ��D
    //
    boolean isRunning()
    {
        return this.isRunning;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    // �T���v���R�[�h�̏������J�n����D
    //
    void open(ConfigTool cfgTool)
    {
        // �����Ԃ��m�F����D
        if (this.isRunning) {
            return;
        }
        // Stream Performer �𐶐����C���o�͏��������s����D
        try {
            // SegmentIo �𐶐�����D�D
            SegmentIo segIo = new SegmentIo();
            
            // SegmentIo �̓��o�͂��\������D�D
            configureInput(cfgTool, segIo);		// IOException
            configureOutput(cfgTool, segIo);	// IOException
            
            // SegmentIo �����ƂɁCStreamPerformer �𐶐�����D
            this.pfmr = StreamPerformer.newInstance(segIo);
            // SystemException, StreamException
            
            // �r���[�����Z�b�g�A�b�v����D
            SwingUtilities.invokeAndWait(() -> setupSimpleViewer());
            // InterruptedException, InvocationTargetException
            
            // ���o�͏������J�n����D
            this.pfmr.open();		// StreamException
            this.pfmr.start();		// StreamException
            
            // �����Ԃ� true �ɂ���D
            this.isRunning = true;
        }
        catch (Exception ex) {
            // ��O�������̃��b�Z�[�W���o�͂���D
            System.out.println("���T���v���R�[�h���s���ɗ�O���������܂����D");
            ex.printStackTrace();
            
            // StreamPerformer �̃C���X�^���X����������Ă���ꍇ�́C
            // StreamPerformer ���I������D
            if (this.pfmr != null) {
                this.pfmr.delete();
                this.pfmr = null;
            }
        }
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    // �T���v���R�[�h�̏������I������D
    //
    void close()
    {
        // �T���v���R�[�h�̏������I������D
        closeSampleCode();
        
        // SimpleViewer �����D
        SwingUtilities.invokeLater(() -> this.viewer.dispose());
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    // �T���v���R�[�h�̊T�v�������擾����D
    //
    abstract String getDescription();
    
    // - PACKAGE METHOD --------------------------------------------------------
    // SegmentIo �̓��͂��\������D
    //
    abstract void configureInput(ConfigTool cfgTool, SegmentIo segIo)
        throws IOException;
    
    // - PACKAGE METHOD --------------------------------------------------------
    // SegmentIo �̏o�͂��\������D
    //
    abstract void configureOutput(ConfigTool cfgTool, SegmentIo segIo)
        throws IOException;
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    // ��EDT��Ŏ��s�����D
    //
    private void setupSimpleViewer()
    {
        // SimpleViewer �̃C���X�^���X�𐶐�����D
        this.viewer = new SimpleViewer(getDescription());
        
        // ���̃C���X�^���X�� SimpleViewer �� WindowListener �Ƃ��ēo�^����D
        this.viewer.addWindowListener(this);
        
        // StreamPerformer ���f���\���ɗ��p���� VideoCanvas ���擾���C
        // SimpleViewer �r���[���ɒǉ�����D
        VideoCanvas vidCvs = this.pfmr.getVideoCanvas();
        this.viewer.addVideoCanvas(vidCvs);
    }
    
    // - PRIVATE METHOD --------------------------------------------------------
    // �T���v���R�[�h�̏������I������D
    //
    private synchronized void closeSampleCode()
    {
        // �����Ԃ��m�F����D
        if (this.isRunning == false) {
            return;
        }
        try {
            // ���o�͏������I������D
            this.pfmr.stop();	// StreamException
            this.pfmr.close();
        }
        catch (StreamException ex) {
            // ��O�������̃X�^�b�N�g���[�X���o�͂���D
            ex.printStackTrace();
        }
        finally {
            // StreamPerformer ���I������D
            if (this.pfmr != null) {
                this.pfmr.delete();
                this.pfmr = null;
            }
            // �����Ԃ� false �ɂ���D
            this.isRunning = false;
        }
    }
}
