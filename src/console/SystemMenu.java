
package console;

import java.util.List;

import com.midfield_system.api.stream.SegmentIo;
import com.midfield_system.api.stream.StreamPerformer;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: SystemMenu
 *
 * Date Modified: 2021.08.23
 *
 */

//==============================================================================
public class SystemMenu
	extends		ConsoleMenu
{
	//- PRIVATE CONSTANT VALUE -------------------------------------------------
	private static final String
		MENU_TITLE				= "���V�X�e�� �����͐ݒ� ���o�͐ݒ� ���X�g���[������",
		MENU_PROMPT				= "sys";
	
	private static final String
		TRANSITION_TO_INPUT		= "�X�g���[�����o�͐ݒ� �� �����͐ݒ�",
		SHOW_STREAM_LIST		= "�X�g���[���ꗗ�\��",
		SELECT_STREAM			= "�X�g���[���I��       �� ���X�g���[������",
		EXIT					= "�I��";
	
//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================

	//- PRIVATE VARIABLE -------------------------------------------------------
	private InputSettingsMenu inputSeggingsMenu = null;
	private StreamControlMenu streamControlMenu = null;
	
//------------------------------------------------------------------------------
//  PUBLIC METHOD:
//------------------------------------------------------------------------------
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public SystemMenu()
	{
		//
	}
	
	//- PUBLIC METHOD ----------------------------------------------------------
	//
	public void setStateInstance(InputSettingsMenu inMenu, StreamControlMenu stmMenu)
	{
		this.inputSeggingsMenu = inMenu;
		this.streamControlMenu = stmMenu;
	}
	
//------------------------------------------------------------------------------
//  PROTECTED METHOD:
//------------------------------------------------------------------------------
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	@Override
	protected MenuItem[] initMenuItems()
	{
		MenuItem[] menuItems = new MenuItem[] {
			new MenuItem(0,	TRANSITION_TO_INPUT,	() -> transitionToInput()),
			new MenuItem(),
			new MenuItem(1,	SHOW_STREAM_LIST,		() -> showStreamList()),
			new MenuItem(2,	SELECT_STREAM,			() -> selectStream()),
			new MenuItem(),
			new MenuItem(9,	EXIT,					() -> exit())
		};
		return menuItems;
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	@Override
	protected String getMenuTitle()
	{
		return MENU_TITLE;
	}
	
	//- PROTECTED METHOD -------------------------------------------------------
	//
	@Override
	protected String getPrompt()
	{
		return MENU_PROMPT;
	}
	
//------------------------------------------------------------------------------
//  PRIVATE METHOD:
//------------------------------------------------------------------------------
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private ConsoleMenu transitionToInput()
	{
		// SegmentIo �ɐݒ肳��Ă�����o�͏����폜����D
		SegmentIo segIo = ConsoleMenu.getSegmentIo();
		segIo.removeInputParams();
		segIo.removeOutputParams();
		
		// ���̃��j���[�ƂȂ� InputSettingsMenu ��Ԃ��D
		return this.inputSeggingsMenu;
	}

	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private ConsoleMenu selectStream()
	{
		// �X�g���[�����I�����ꂽ�ꍇ�C
		// ���̃��j���[�� StreamControlMenu �ƂȂ�D
		ConsoleMenu nextMenu = this.streamControlMenu;
		
		// ���쒆�� StreamPerformer �̃��X�g���擾����D
		List<StreamPerformer> lsPfmr = StreamPerformer.getStreamPerformerList();
		int size = lsPfmr.size();
		if (size > 0) {
			// ���X�g���ꗗ�\�����āC�X�g���[���ԍ��̓��͂�҂D
			printStreamPerformerList(lsPfmr);
			int num = selectNumber("�X�g���[���ԍ�");
			if ((num >= 0) && (num < size)) {
				// ���͂��ꂽ�X�g���[���ԍ��� StreamPerformer ���擾����D
				StreamPerformer pfmr = lsPfmr.get(num);
				
				// ���̃��j���[�ł��� StreamControlMenu ��
				// �擾���� StreamPerformer �𗘗p�ł���悤�C
				// ConsoleMenu �̃N���X�ϐ��ɐݒ肵�Ă����D
				ConsoleMenu.setSelectedStream(pfmr);
			}
			else {
				// ���͂��ꂽ�X�g���[���ԍ��� StreamPerformer �͖����D
				// ���̃��j���[�� SystemMenu �̂܂܁D
				warningPause("�L�����Z�����܂����D\n");
				nextMenu = this;
			}
		}
		else {
			// ���쒆�� StreamPerformer �͖����D
			// ���̃��j���[�� SystemMenu �̂܂܁D
			warningPause("�I���ł���X�g���[��������܂���D\n");
			nextMenu = this;
		}
		// ���̃��j���[��Ԃ��D
		return nextMenu;
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private void printStreamPerformerList(List<StreamPerformer> lsPfmr)
	{
		int size = lsPfmr.size();
		for (int i = 0; i < size; i++) {
			StreamPerformer pfmr = lsPfmr.get(i);
			SegmentIo segIo = pfmr.getSegmentIo();
			
			print("\n");
			print("���X�g���[��[%s] �`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`\n",
				Integer.toString(i)
			);
			printIoParamList("���͐ݒ�", segIo.getInputParamList());
			printIoParamList("�o�͐ݒ�", segIo.getOutputParamList());
			print("�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`�`\n");
		}
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private ConsoleMenu showStreamList()
	{
		// ���쒆�� StreamPerformer �̃��X�g���擾����D
		List<StreamPerformer> lsPfmr = StreamPerformer.getStreamPerformerList();
		int size = lsPfmr.size();
		if (size <= 0) {
			warningPause("�X�g���[��������܂���D\n");
			return this;
		}
		// �擾�������X�g��\������D
		printStreamPerformerList(lsPfmr);
		pause();
		
		return this;
	}
	
	//- PRIVATE METHOD ---------------------------------------------------------
	//
	private ConsoleMenu exit()
	{
		// ���̃��j���[�͖����D
		// ConsoleMenu �ɂ�鏈���̏I�������� null ��Ԃ��D
		return null;
	}
}