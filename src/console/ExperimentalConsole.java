
package console;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.midfield_system.api.system.MfsNode;
import com.midfield_system.api.system.SystemException;
import com.midfield_system.api.util.Log;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: ExperimentalConsole
 *
 * Date Modified: 2021.10.07
 *
 */
public class ExperimentalConsole
{
// =============================================================================
//  CLASS METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
//  PUBLIC STATIC METHOD:
// -----------------------------------------------------------------------------

	//======== [MAIN METHOD] ===================================================
	//
	public static void main(String[] args)
	{
		ExperimentalConsole console = null;
		try {
			// ExperimentalConsole �̃C���X�^���X�𐶐�����D
			console = new ExperimentalConsole();
				// InvocationTargetException, InterruptedException,
				// SystemException
			
			// ���������C���X�^���X�ɂ�鏈�����J��Ԃ����s����D
			console.mainLoop();
		}
		catch (InvocationTargetException | InterruptedException ex) {
			// GUI�R���|�[�l���g�̃Z�b�g�A�b�v�Ɏ��s�����D
			ex.printStackTrace();
		}
		catch (SystemException ex) {
			// MidField System �̋N���Ɏ��s�����D
			ex.printStackTrace();
		}
		finally {
			// �I������������D
			if (console != null) {
				console.close();
			}
		}
	}
	
// =============================================================================
//  INSTANCE VARIABLE:
// =============================================================================

	// - PRIVATE VARIABLE ------------------------------------------------------
	private final MfsNode mfs;
	private ExConsoleFrame consoleFrame = null;
	
// =============================================================================
//  INSTANCE METHOD:
// =============================================================================

// -----------------------------------------------------------------------------
//  PACKAGE METHOD:
// -----------------------------------------------------------------------------
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	ExperimentalConsole()
		throws	InvocationTargetException,
				InterruptedException,
				SystemException
	{
		// GUI�R���|�[�l���g���Z�b�g�A�b�v����D
		SwingUtilities.invokeAndWait(() -> { this.consoleFrame = setupGui(); });
			// InvocationTargetException,
			// InterruptedException
			
		// MidField System ������������D
		this.mfs = MfsNode.initialize();
			// SystemException
			
		// �C�x���g���X�i��o�^����D
		this.mfs.addSystemEventListener(this.consoleFrame);
			
		// MidField System ���N������D
		this.mfs.activate();
			// SystemException
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	void mainLoop()
	{
		// ConsoleMenu ���g�������e�N���X�̃C���X�^���X(���j���[)�𐶐����C
		// �ŏ��ɗ��p���郁�j���[�̎Q�Ƃ��擾����D
		ConsoleMenu menu = setupConsoleMenu();
		
		// ���j���[���̏������J��Ԃ����s����D
		while (menu != null) {
			try {
				// ���݂̃��j���[�ɂ�����A�N�V���������s���C
				// ���̃��j���[�̎Q�Ƃ��擾����D
				// ���̃��j���[�̎Q�Ƃ� null �̏ꍇ�́C
				// ���̃��[�v�𔲂���D
				menu = menu.action();
			}
			catch (Exception ex) {
				menu.warning(ex.getMessage());
			}
		}
	}
	
	// - PACKAGE METHOD --------------------------------------------------------
	//
	void close()
	{
		if (MfsNode.isActive() == false) {
			return;
		}
		// MidField System �ɓo�^�����C�x���g���X�i�̐ݒ���������C
		// MidField System �̏������I������D
		this.mfs.removeSystemEventListener(this.consoleFrame);
		this.mfs.shutdown();
		
		// ExConsoleFrame ���I������D
		if (this.consoleFrame != null) {
			SwingUtilities.invokeLater(() -> this.consoleFrame.dispose());
		}
	}
	
// -----------------------------------------------------------------------------
//  PRIVATE METHOD:
// -----------------------------------------------------------------------------
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private ExConsoleFrame setupGui()
	{
		// ExConsoleFrame �𐶐�����D
		ExConsoleFrame frame = new ExConsoleFrame();
		
		// ExperimentalConsole �̓��͌���ݒ肷��D
		ConsoleMenu.setLineReader(frame.getLineReader());
		
		// ExperimentalConsole �̏o�͐��ݒ肷��D
		ConsoleMenu.setLogPrinter(frame.getConsolePrinter());
		
		// MidField System �̃��O�̏o�͐��ݒ肷��D
		Log.setLogPrinter(frame.getSystemLogPrinter());
		
		// ExConsoleFrame �̃C���X�^���X��Ԃ��D
		return frame;
	}
	
	// - PRIVATE METHOD --------------------------------------------------------
	//
	private ConsoleMenu setupConsoleMenu()
	{
		// �e���j���[�̃C���X�^���X�𐶐�����D
		SystemMenu			sysMenu = new SystemMenu();
		InputSettingsMenu	inMenu  = new InputSettingsMenu();
		OutputSettingsMenu	outMenu = new OutputSettingsMenu();
		StreamControlMenu	stmMenu = new StreamControlMenu();
		
		// �e���j���[����J�ڂ��郁�j���[�̃C���X�^���X��ݒ肷��D
		sysMenu.setStateInstance(inMenu,  stmMenu);
		inMenu.setStateInstance( sysMenu, outMenu);
		outMenu.setStateInstanue(sysMenu, stmMenu);
		stmMenu.setStateInstance(sysMenu);
		
		// �������j���[�Ƃ��� SystemMenu �̃C���X�^���X��Ԃ��D
		return sysMenu;
	}
}
