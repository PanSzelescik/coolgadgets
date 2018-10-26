package panszelescik.coolgadgets.gui;

import static panszelescik.coolgadgets.CoolGadgets.*;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import panszelescik.morelibs.api.Helper;
import panszelescik.morelibs.api.MathHelper;
import panszelescik.morelibs.api.NBTHelper;
import panszelescik.morelibs.gui.GuiImageButton;

public class CalculatorGUI extends GuiScreen {
	
	public ItemStack stack;
	private static final String defaultText = "0.0";
	private GuiTextField text;
	private boolean firstTyped;
	private static final String button = "textures/gui/button.png";
	
	public CalculatorGUI(ItemStack stack) {
		this.stack = stack;
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void initGui() {
		this.buttonList.clear();
		int offsetX = width / 4;
		int offsetY = height / 4;
		final int numButtonsX = 4;
		final int numButtonsY = 5;
		int sizeX = width / (2 * numButtonsX);
		int sizeY = height / (2 * numButtonsY);
		Object[] calcSigns = {
				Helper.translate("gui." + MODID + ".calculator.button.element"), "pi", "<--", "/",
				7, 8, 9, "*",
				4, 5, 6, "-",
				1, 2, 3, "+",
				0, ".", "C", "=",
		};
		ArrayList<Object> calcSigns1 = new ArrayList();
		for (Object a : calcSigns) {
			calcSigns1.add(a);
		}
		int i = 0;
		for (int yi = 0; yi < numButtonsY; yi++) {
			for (int xi = 0; xi < numButtonsX; xi++) {
				this.buttonList.add(new GuiImageButton(i, offsetX + sizeX * xi, offsetY + sizeY * yi, sizeX - 2, sizeY - 2, new ResourceLocation[]{new ResourceLocation(MODID, button)}, calcSigns1.get(i).toString()));
				i++;
			}
		}
		text = new GuiTextField(20, Minecraft.getMinecraft().fontRenderer, offsetX, offsetY - sizeY, (offsetX * 2) - 2, sizeY - 2);
		text.setMaxStringLength(32);
		text.setText(NBTHelper.getString(stack, "display", defaultText));
		firstTyped = NBTHelper.getBoolean(stack, "firstTyped", true);
	}
	
	public void drawScreen(int a, int b, float c) {
		super.drawScreen(a, b, c);
		text.drawTextBox();
	}
	
	protected void keyTyped(char character, int key) throws IOException {
		if (Character.isDigit(character)) {
			addText(String.valueOf(character));
		}
		super.keyTyped(character, key);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (!button.enabled) {
			return;
		}
		String key = getKeyFromID(button.id);
		switch (key) {
			case "C":
				clearText();
				break;
			case "<--":
				if (!firstTyped) {
					setText(text.getText().substring(0, text.getText().length() - 1));
				}
				if (text.getText().isEmpty()) {
					clearText();
				}
				break;
			case "=":
				try {
					equals();
				} catch (RuntimeException e) {
					logger.error(e);
					clearText();
				}
				break;
			case "element":
				try {
					equals();
					setText(String.valueOf(Math.sqrt(Double.parseDouble(text.getText()))));
				} catch (RuntimeException e) {
					logger.error(e);
					clearText();
				}
				break;
			default:
				addText(key);
				break;
		}
	}
	
	private void setText(String txt) {
		text.setText(txt);
		NBTHelper.setString(stack, "display", txt);
	}
	
	private void setBoolean(boolean typed) {
		firstTyped = typed;
		NBTHelper.setBoolean(stack, "firstTyped", typed);
	}
	
	private void addText(String number) {
		if (firstTyped) {
			setText(number);
			setBoolean(false);
		} else {
			setText(text.getText() + number);
		}
	}
	
	private void clearText() {
		setText(defaultText);
		setBoolean(true);
	}
	
	private void equals() {
		setText(String.valueOf(MathHelper.eval(text.getText())));
	}
	
	private String getKeyFromID(int id) {
		switch (id) {
			case 0:
				return "element";
			case 1:
				return String.valueOf(Math.PI);
			case 2:
				return "<--";
			case 3:
				return "/";
			case 4:
				return "7";
			case 5:
				return "8";
			case 6:
				return "9";
			case 7:
				return "*";
			case 8:
				return "4";
			case 9:
				return "5";
			case 10:
				return "6";
			case 11:
				return "-";
			case 12:
				return "1";
			case 13:
				return "2";
			case 14:
				return "3";
			case 15:
				return "+";
			case 16:
				return "0";
			case 17:
				return ".";
			case 18:
				return "C";
			case 19:
				return "=";
		}
		return null;
	}
}