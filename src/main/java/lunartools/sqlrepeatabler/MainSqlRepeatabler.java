package lunartools.sqlrepeatabler;

import java.util.Objects;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.formdev.flatlaf.FlatLightLaf;

import lunartools.sqlrepeatabler.bootstrap.ApplicationBootstrap;
import lunartools.sqlrepeatabler.common.ui.Dialogs;

/*
 * Copyright (c) 2025 Thomas Mattel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
public class MainSqlRepeatabler {
	private static Logger logger = LoggerFactory.getLogger(MainSqlRepeatabler.class);

	public static void main(String[] args) {
		FlatLightLaf.setup();
		SwingUtilities.invokeLater(() -> {
			try{
				ApplicationBootstrap.start();
			}catch(Throwable e){
				logger.error("Unexpected error during application startup",e);
				Dialogs.showErrorMessage("Application failed to start:\n"+Objects.toString(e.getMessage(), e.getClass().getName()));
				System.exit(1);
			}
		});
	}

}
