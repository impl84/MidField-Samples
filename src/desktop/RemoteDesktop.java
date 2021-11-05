
package desktop;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.system.SystemException;
import com.midfield_system.api.util.Log;
import com.midfield_system.gui.misc.MessagePanel;

import util.AppUtilities;
import util.Dialog;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: RemoteDesktop
 *
 * Date Modified: 2021.09.20
 *
 */

//==============================================================================
@SuppressWarnings("serial")
public class RemoteDesktop
	extends		JFrame
	implements	ActionListener
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String
		STR_FRAME_TITLE			= "���u�f�X�N�g�b�v����";
	
	private static final String	
		STR_ACCEPTANCE			= "���u������󂯓����",
		STR_NEW_REMOCON			= "�V�������u����p�E�B���h�E�𐶐�����";
	
	private static final String
		STR_OPEN_SERVER			= "���u����̎󂯓�����J�n���܂��D",
		STR_CLOSE_SERVER		= "���u����̎󂯓�����I�����܂��D";
	
	private static final String
		STR_ALREADY_IN_USE		= "���ɉ��u����󂯓��ꏈ�����ł��D",
		STR_NOT_RUNNING			= "���u����󂯓��ꏈ���͍s���Ă��܂���D";
	
	private static final Dimension DIM_FRAME = new Dimension(640, 480);
	
//==============================================================================
//  CLASS METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PUBLIC STATIC METHOD:
//------------------------------------------------------------------------------
	
	//- PUBLIC STATIC METHOD ---------------------------------------------------
	//
	public static void main(String[] args)
	{
		RemoteDesktop.launch(args);
	}
	
	//- PUBLIC STATIC METHOD ---------------------------------------------------
	//
	public static void launch(String[] args)
	{
		SwingUtilities.invokeLater(() -> {
			try {
				// RemoteDesktop �̃C���X�^���X�𐶐�����D
				new RemoteDesktop(args);
					// SystemException
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}
	
//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================
	
	//- PRIVATE VARIABLE -------------------------------------------------------
	private final MfsNode mfs;
	private final List<DesktopViewer> viewerList;
	
	private DesktopServer server = null;
	
//==============================================================================
//  INSTANCE METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PUBLIC METHOD: ActionListener
//------------------------------------------------------------------------------	
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//- IMPLEMENTS: ActionListener
	//
	@Override
	public void actionPerformed(ActionEvent ev)
	{
		String actCmd = ev.getActionCommand();
		Object obj = ev.getSource();
		
		switch (actCmd) {
		case STR_ACCEPTANCE:	evHn_Acceptance((JToggleButton)obj);break;
		case STR_NEW_REMOCON:	evHn_Control();		break;
		default:									break;
		}
	}
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	void removeViewer(DesktopViewer viewer)
	{
		this.viewerList.remove(viewer);
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD:
//------------------------------------------------------------------------------
	
	//- CONSTRUCTOR ------------------------------------------------------------
	//	
	private RemoteDesktop(String[] args)
		throws	SystemException
	{
		// GUI���Z�b�g�A�b�v����D
		setupGui();
		
		// MidField System ���N������D
		this.mfs = MfsNode.initialize();	// SystemException
		this.mfs.activate();				// SystemException
		
		// DesktopViewer �̃��X�g�𐶐�����D
		this.viewerList = new ArrayList<DesktopViewer>();
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void setupGui()
	{
		// MessagePanel �𐶐����C���O�̏o�͐�Ƃ��Đݒ肷��D
		MessagePanel panel = new MessagePanel();
		Log.setLogPrinter(panel);
		
		// �{�^���ƃR���g���[���{�b�N�X�𐶐�����D
		JToggleButton btnAcceptance = new JToggleButton(STR_ACCEPTANCE);
		btnAcceptance.setActionCommand(STR_ACCEPTANCE);
		btnAcceptance.addActionListener(this);
		
		JButton btnControl = new JButton(STR_NEW_REMOCON);
		btnControl.setActionCommand(STR_NEW_REMOCON);
		btnControl.addActionListener(this);
		
		Box box = Box.createHorizontalBox();
		box.add(btnAcceptance);
		box.add(btnControl);
		
		// ���� JFrame �̃R���e�i����
		// ���b�Z�[�W�p�l���ƃR���g���[���{�b�N�X��ǉ�����D
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(box, BorderLayout.NORTH);
		container.add(panel, BorderLayout.CENTER);
		
		// L&F ��ݒ肷��D
		AppUtilities.setLookAndFeel(this);
		
		// �^�C�g���ƍŏ��T�C�Y��ݒ肷��D
		setTitle(STR_FRAME_TITLE);
		setMinimumSize(DIM_FRAME);
		setPreferredSize(DIM_FRAME);
		pack();
		
		// ��ʒ����Ƀt���[���̈ʒu�����킹��D
		AppUtilities.setLocationToCenter(this);
		
		// �I��������o�^����D
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent ev) {
				close();
			}
		});
		// ����Ԃɂ���D
		setVisible(true);
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void close()
	{
		// ���쒆�� DesktopViewer ������ΑS�ďI������D
		for (DesktopViewer viewer : this.viewerList) {
			viewer.close();
		}
		// DesktopServer �����쒆�ł���ΏI������D
		if (this.server != null) {
			Log.message(STR_CLOSE_SERVER);
			this.server.close();
		}
		// MidField System ���I������D
		this.mfs.shutdown();
		
		// ���̃t���[���̏I�����������s����D
		dispose();
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD: ActionEvent �n���h��
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_Acceptance(JToggleButton btn)
	{
		// ���u����󂯓���p�g�O���{�^�����I����Ԃ��ǂ������m�F����D
		if (btn.isSelected()) {
			// ���u����󂯓���p�g�O���{�^�����I����ԂŁC
			// DesktopServer �̃C���X�^���X�����݂��Ȃ��ꍇ�C
			// DesktopServer �̃C���X�^���X�𐶐�����D
			if (this.server == null) {
				try {
					Log.message(STR_OPEN_SERVER);
					this.server = new DesktopServer(this);
						// SystemException
				}
				catch (SystemException ex) {
					Log.error(ex);
					Dialog.error(this, ex);
				}
			}
			else {
				Log.error(STR_ALREADY_IN_USE);
				Dialog.error(this, STR_ALREADY_IN_USE);
			}
		}
		else {
			// ���u����󂯓���p�g�O���{�^������I����ԂŁC
			// DesktopServer �̃C���X�^���X�����݂���ꍇ�C
			// DesktopServer �̃C���X�^���X���������D
			if (this.server != null) {
				Log.message(STR_CLOSE_SERVER);
				this.server.close();
				this.server = null;
			}
			else {
				Log.error(STR_NOT_RUNNING);
				Dialog.error(this, STR_NOT_RUNNING);
			}
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//	
	private void evHn_Control()
	{
		// DesktopClient �̃C���X�^���X�𐶐����C���X�g�֒ǉ�����D
		DesktopViewer viewer = new DesktopViewer(this);
		this.viewerList.add(viewer);
	}
}