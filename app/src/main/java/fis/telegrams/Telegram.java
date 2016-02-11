/* Copyright 2016 Erik Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
 *
 * This file is part of FIS.
 *
 * FIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FIS.  If not, see <http://www.gnu.org/licenses/>.
 */
package fis.telegrams;

import java.nio.charset.Charset;

/**
 * Abstrakte Klasse, die die Telegramme kapselt.
 *
 * @author spiollinux, Robert
 */
public abstract class Telegram {
	// Codepage für String-Konvertierung
	public static final Charset CHARSET = Charset.forName("ISO-8859-1");
	// Endianness für die Wort-Konvertierung bei Zeiten
	public static final boolean LITTLE_ENDIAN = true;

	@Override
	public abstract String toString();

	@Override
	public abstract boolean equals(Object other);
}
