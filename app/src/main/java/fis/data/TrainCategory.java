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
package fis.data;

/**
 * Speichert eine bestimmte Zugkategorie
 *
 * @author Luux
 */
public class TrainCategory {
	private String id;
	private String name;
	private String description;
	private String trainUsage;

	/**
	 * @param id          ID
	 * @param name        Name
	 * @param description Beschreibung
	 * @param trainUsage  Nutzung der Züge dieser Kategorie
	 */
	public TrainCategory(String id, String name, String description, String trainUsage) {
		if (id == null || name == null || description == null || trainUsage == null)
			throw new IllegalArgumentException("id, name, description und trainUsage dürfen nicht null sein!");

		this.id = id;
		this.name = name;
		this.description = description;
		this.trainUsage = trainUsage;
	}

	/**
	 * Getter für id
	 *
	 * @return ID dieser Kategorie
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter für name
	 *
	 * @return Name dieser Zugkategorie
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter für description
	 *
	 * @return Beschreibung dieser Zugkategorie
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Getter für trainUsage
	 *
	 * @return Nutzung der Züge dieser Kategorie
	 */
	public String getTrainUsage() {
		return trainUsage;
	}

	/**
	 * Liefert menschenlesbare Reptäsentation der {@link TrainCategory}
	 *
	 * @return String der Form [RE] RegionalExpress
	 */
	@Override
	public String toString() {
		return String.format("[%s] %s", this.name, this.description);
	}
}
