/*
 * MIT License
 *
 * Copyright (c) 2023 Hydrologic Engineering Center
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package mil.army.usace.hec.cwms.radar.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

//This class is not codegenned as there is no OpenAPI docs for the parameter catalog
public final class Parameter {

    @JsonProperty(value = "abstract-param")
    private String abstractParam;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "office")
    private String office;
    @JsonProperty(value = "default-si-unit")
    private String defaultSiUnit;
    @JsonProperty(value = "default-english-unit")
    private String defaultEnglishUnit;
    @JsonProperty(value = "long-name")
    private String longName;
    @JsonProperty(value = "description")
    private String description;

    public String abstractParam() {
        return abstractParam;
    }

    public String getName() {
        return name;
    }

    public String office() {
        return office;
    }

    public String defaultSiUnit() {
        return defaultSiUnit;
    }

    public String defaultEnglishUnit() {
        return defaultEnglishUnit;
    }

    public String longName() {
        return longName;
    }

    public String description() {
        return description;
    }
}
