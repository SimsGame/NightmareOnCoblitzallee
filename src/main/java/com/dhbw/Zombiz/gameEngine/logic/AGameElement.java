/**
 * *****************************************************************************
 * Copyright (c) 2013 DHBW. This source is subject to the DHBW Permissive
 * License. Please see the License.txt file for more information. All other
 * rights reserved.
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 * EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Project: Zombiz Package: com.dhbw.zombiz.output.audio
 *
 * Contributors: -Christoph Schabert
 *
 *******************************************************************************
 */
package com.dhbw.Zombiz.gameEngine.logic;

import java.io.Serializable;

/**
 * Comments still missing ...
 *
 * @author Christoph Schabert
 */
@SuppressWarnings("serial")
public abstract class AGameElement implements Serializable {

    final int id;

    public AGameElement(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
