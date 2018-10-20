package panszelescik.coolgadgets.gui;

import static panszelescik.coolgadgets.CoolGadgets.*;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import panszelescik.morelibs.api.MathHelper;
import panszelescik.morelibs.gui.GuiImageButton;

public class CalculatorGUI extends GuiScreen {
	
	private static final String defaultText = "0.0";
	private GuiTextField text;
	private boolean firstTyped;
	private static final String button = "textures/gui/button.png";
	
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
	    final int numButtonsY = 4;
        int sizeX = width / (2 * numButtonsX);
        int sizeY = height / (2 * numButtonsY);
        Object[] calcSigns = {
        		7, 8, 9, "/",
        		4, 5, 6, "*",
        		1, 2, 3, "+",
        		0, ".", "C", "-",
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
        this.buttonList.add(new GuiImageButton(16, offsetX, offsetY * 3, (offsetX * 2) - 2 , sizeY - 2, new ResourceLocation[]{new ResourceLocation(MODID, button)}, "="));
		
		text = new GuiTextField(17, Minecraft.getMinecraft().fontRenderer, offsetX, offsetY - sizeY, (offsetX * 2) - 2, sizeY - 2);
		text.setText(defaultText);
		firstTyped = true;
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
		int id = button.id;
		String key = getKeyFromID(id);
		if (key.equals("C")) {
			text.setText(defaultText);
			firstTyped = true;
		} else if (key.equals("=")) {
			try {
				text.setText(String.valueOf(MathHelper.eval(text.getText())));
			} catch (RuntimeException e) {
				text.setText(defaultText);
				firstTyped = true;
			}
		} else {
			addText(key);
		}
	}
	
	private void addText(String number) {
		if (firstTyped) {
			text.setText(number);
			firstTyped = false;
		} else {
			text.setText(text.getText() + number);
		}
	}
	
	private String getKeyFromID(int id) {
		switch (id) {
			case 0:
				return "7";
			case 1:
				return "8";
			case 2:
				return "9";
			case 3:
				return "/";
			case 4:
				return "4";
			case 5:
				return "5";
			case 6:
				return "6";
			case 7:
				return "*";
			case 8:
				return "1";
			case 9:
				return "2";
			case 10:
				return "3";
			case 11:
				return "+";
			case 12:
				return "0";
			case 13:
				return ".";
			case 14:
				return "C";
			case 15:
				return "-";
			case 16:
				return "=";
		}
		return null;
	}
}