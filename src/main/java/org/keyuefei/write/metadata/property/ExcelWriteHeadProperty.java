package org.keyuefei.write.metadata.property;


import org.keyuefei.annotation.write.style.*;
import org.keyuefei.write.metadata.head.ExcelHead;
import org.keyuefei.write.metadata.holder.AbstractWriteHolder;

import java.lang.reflect.Field;


public class ExcelWriteHeadProperty extends ExcelHeadProperty {

    private RowHeightProperty headRowHeightProperty;
    private RowHeightProperty contentRowHeightProperty;
    private OnceAbsoluteMergeProperty onceAbsoluteMergeProperty;

    public ExcelWriteHeadProperty(AbstractWriteHolder holder, Class headClazz) {
        super(holder, headClazz);

        this.headRowHeightProperty = RowHeightProperty.build((HeadRowHeight) headClazz.getAnnotation(HeadRowHeight.class));
        this.contentRowHeightProperty =
                RowHeightProperty.build((ContentRowHeight) headClazz.getAnnotation(ContentRowHeight.class));
        this.onceAbsoluteMergeProperty =
                OnceAbsoluteMergeProperty.build((OnceAbsoluteMerge) headClazz.getAnnotation(OnceAbsoluteMerge.class));

        ColumnWidth parentColumnWidth = (ColumnWidth) headClazz.getAnnotation(ColumnWidth.class);
        HeadStyle parentHeadStyle = (HeadStyle) headClazz.getAnnotation(HeadStyle.class);
        HeadFontStyle parentHeadFontStyle = (HeadFontStyle) headClazz.getAnnotation(HeadFontStyle.class);
        ContentStyle parentContentStyle = (ContentStyle) headClazz.getAnnotation(ContentStyle.class);
        ContentFontStyle parentContentFontStyle = (ContentFontStyle) headClazz.getAnnotation(ContentFontStyle.class);

        for (ExcelHead head : getHeads()) {

            Field field = head.getField();
            ColumnWidth columnWidth = field == null ? null : field.getAnnotation(ColumnWidth.class);
            if (columnWidth == null) {
                columnWidth = parentColumnWidth;
            }
            head.setColumnWidthProperty(ColumnWidthProperty.build(columnWidth));

            HeadStyle headStyle = field == null ? null : field.getAnnotation(HeadStyle.class);
            if (headStyle == null) {
                headStyle = parentHeadStyle;
            }
            head.setHeadStyleProperty(StyleProperty.build(headStyle));

            HeadFontStyle headFontStyle = field == null ? null : field.getAnnotation(HeadFontStyle.class);
            if (headFontStyle == null) {
                headFontStyle = parentHeadFontStyle;
            }
            head.setHeadFontProperty(FontProperty.build(headFontStyle));

            ContentStyle contentStyle = field == null ? null : field.getAnnotation(ContentStyle.class);
            if (contentStyle == null) {
                contentStyle = parentContentStyle;
            }
            head.setContentStyleProperty(StyleProperty.build(contentStyle));

            ContentFontStyle contentFontStyle = field == null ? null : field.getAnnotation(ContentFontStyle.class);
            if (contentFontStyle == null) {
                contentFontStyle = parentContentFontStyle;
            }
            head.setContentFontProperty(FontProperty.build(contentFontStyle));
        }
    }


}
