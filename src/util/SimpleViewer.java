
package util;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * 
 * Sample code for MidField API
 *
 * Date Modified: 2025.11.12
 *
 */
@SuppressWarnings("serial")
public class SimpleViewer
    extends
        JFrame
{
    // - PRIVATE CONSTANT VALUE ------------------------------------------------
    private static final int DEF_WIDTH  = 640;  // ビューワのデフォルトの幅 [pixel]
    private static final int DEF_HEIGHT = 480;  // ビューワのデフォルトの高さ [pixel]
    
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
    public SimpleViewer(String title, Canvas canvas)
    {
        this(title);
        addCanvas(canvas);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public SimpleViewer(String title, int width, int height)
    {
        // GUIをセットアップする．
        setupGui(title, width, height);
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void addCanvas(Canvas canvas)
    {
        if (canvas == null) {
            return;
        }
        Container container = getContentPane();
        container.add(canvas);
        validate();
    }
    
    // - PUBLIC METHOD ---------------------------------------------------------
    //
    public void removeCanvas(Canvas canvas)
    {
        if (canvas == null) {
            return;
        }
        Container container = getContentPane();
        container.remove(canvas);
        validate();
    }
    
// -----------------------------------------------------------------------------
// PRIVATE METHOD:
// -----------------------------------------------------------------------------
    
    // - PRIVATE METHOD --------------------------------------------------------
    //
    private void setupGui(String title, int width, int height)
    {
        // JFrame のタイトルと終了処理を設定する．
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        // JFrame から Content Pane を取り出し，背景色を青に設定する．
        Container container = getContentPane();
        container.setBackground(Color.BLUE);
        
        // Content Pane の推奨サイズを，与えられた幅と高さそれぞれの
        // 4分の1 に設定した上で，
        // JFrame のサイズを Content Pane の推奨サイズに合わせる．
        // 次に JFrame のサイズを取得して，それを JFrame の最小サイズとして設定する．
        container.setPreferredSize(new Dimension(width / 4, height / 4));
        pack();
        Dimension dim = getSize();
        setMinimumSize(dim);
        
        // Content Pane の推奨サイズを，与えられた幅と高さに設定し，
        // JFrame のサイズを Content Pane の推奨サイズに合わせる．
        container.setPreferredSize(new Dimension(width, height));
        pack();
        
        // JFrame をスクリーンの真中に配置する．
        AppUtilities.setLocationToCenter(this);
        
        // JFrame を表示する．
        setVisible(true);
    }
}
