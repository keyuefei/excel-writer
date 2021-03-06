package org.keyuefei.write.metadata.property;


import org.keyuefei.annotation.write.style.ContentRowHeight;
import org.keyuefei.annotation.write.style.HeadRowHeight;

public class RowHeightProperty {
    private Short height;

    public RowHeightProperty(Short height) {
        this.height = height;
    }

    public static RowHeightProperty build(HeadRowHeight headRowHeight) {
        if (headRowHeight == null || headRowHeight.value() < 0) {
            return null;
        }
        return new RowHeightProperty(headRowHeight.value());
    }

    public static RowHeightProperty build(ContentRowHeight contentRowHeight) {
        if (contentRowHeight == null || contentRowHeight.value() < 0) {
            return null;
        }
        return new RowHeightProperty(contentRowHeight.value());
    }

    public Short getHeight() {
        return height;
    }

    public void setHeight(Short height) {
        this.height = height;
    }
}
