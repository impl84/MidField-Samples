
package util;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.midfield_system.api.viewer.VideoCanvas;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2021.09.19
 *
 */
@SuppressWarnings("serial")
public class SimpleViewer
    extends
        JFrame
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final int DEF_WIDTH  = 640;  // �r���[���̃f�t�H���g�̕� [pixel]
    private static final int DEF_HEIGHT = 480;  // �r���[���̃f�t�H���g�̍��� [pixel]
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD:
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public SimpleViewer(String title)
    {
        this(title, DEF_WIDTH, DEF_HEIGHT);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public SimpleViewer(String title, VideoCanvas vidCvs)
    {
        this(title);
        addVideoCanvas(vidCvs);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public SimpleViewer(String title, int width, int height)
    {
        // GUI���Z�b�g�A�b�v����D
        setupGui(title, width, height);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void addVideoCanvas(VideoCanvas vidCvs)
    {
        if (vidCvs == null) {
            return;
        }
        Container container = getContentPane();
        container.add(vidCvs);
        validate();
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void removeVideoCanvas(VideoCanvas vidCvs)
    {
        if (vidCvs == null) {
            return;
        }
        Container container = getContentPane();
        container.remove(vidCvs);
        validate();
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void setupGui(String title, int width, int height)
    {
        // JFrame �̃^�C�g���ƏI��������ݒ肷��D
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        // JFrame ���� Content Pane �����o���C�w�i�F��ɐݒ肷��D
        Container container = getContentPane();
        container.setBackground(Color.BLUE);
        
        // Content Pane �̐����T�C�Y���C�^����ꂽ���ƍ������ꂼ���
        // 4����1 �ɐݒ肵����ŁC
        // JFrame �̃T�C�Y�� Content Pane �̐����T�C�Y�ɍ��킹��D
        // ���� JFrame �̃T�C�Y���擾���āC����� JFrame �̍ŏ��T�C�Y�Ƃ��Đݒ肷��D
        container.setPreferredSize(new Dimension(width / 4, height / 4));
        pack();
        Dimension dim = getSize();
        setMinimumSize(dim);
        
        // Content Pane �̐����T�C�Y���C�^����ꂽ���ƍ����ɐݒ肵�C
        // JFrame �̃T�C�Y�� Content Pane �̐����T�C�Y�ɍ��킹��D
        container.setPreferredSize(new Dimension(width, height));
        pack();
        
        // JFrame ���X�N���[���̐^���ɔz�u����D
        AppUtilities.setLocationToCenter(this);
        
        // JFrame ��\������D
        setVisible(true);
    }
}
