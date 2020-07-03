package org.keyuefei.write.metadata.holder;

import org.keyuefei.write.accumulator.Accumulator;
import org.keyuefei.write.accumulator.DefaultAccumulator;
import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.converters.ConverterKeyBuild;
import org.keyuefei.write.converters.DefaultConverterLoader;
import org.keyuefei.write.metadata.WriteBasicParameter;

import java.util.Collection;
import java.util.HashMap;

public abstract class AbstractWriteHolder extends AbstractHolder implements WriteHolder {
    /**
     * Need Head
     */
    private Boolean needHead;
    /**
     * Writes the head relative to the existing contents of the sheet. Indexes are zero-based.
     */
    private Integer relativeHeadRowIndex;
    /**
     * Use the default style.Default is true.
     */
    private Boolean useDefaultStyle;
    /**
     * Whether to automatically merge headers.Default is true.
     */
    private Boolean automaticMergeHead;
    /**
     * Ignore the custom columns.
     */
    private Collection<String> excludeColumnFiledNames;
    /**
     * Only output the custom columns.
     */
    private Collection<String> includeColumnFiledNames;
    /**
     * 累加器
     */
    private Accumulator accumulator;


    public AbstractWriteHolder(WriteBasicParameter writeBasicParameter, AbstractWriteHolder parentAbstractWriteHolder) {
        super(writeBasicParameter, parentAbstractWriteHolder);

        if (writeBasicParameter.getRelativeHeadRowIndex() == null) {
            if (parentAbstractWriteHolder == null) {
                this.relativeHeadRowIndex = 0;
            } else {
                this.relativeHeadRowIndex = parentAbstractWriteHolder.getRelativeHeadRowIndex();
            }
        } else {
            this.relativeHeadRowIndex = writeBasicParameter.getRelativeHeadRowIndex();
        }

        if (writeBasicParameter.getUseDefaultStyle() == null) {
            if (parentAbstractWriteHolder == null) {
                this.useDefaultStyle = Boolean.TRUE;
            } else {
                this.useDefaultStyle = parentAbstractWriteHolder.getUseDefaultStyle();
            }
        } else {
            this.useDefaultStyle = writeBasicParameter.getUseDefaultStyle();
        }


        if (writeBasicParameter.getExcludeColumnFiledNames() == null && parentAbstractWriteHolder != null) {
            this.excludeColumnFiledNames = parentAbstractWriteHolder.getExcludeColumnFiledNames();
        } else {
            this.excludeColumnFiledNames = writeBasicParameter.getExcludeColumnFiledNames();
        }
        if (writeBasicParameter.getIncludeColumnFiledNames() == null && parentAbstractWriteHolder != null) {
            this.includeColumnFiledNames = parentAbstractWriteHolder.getIncludeColumnFiledNames();
        } else {
            this.includeColumnFiledNames = writeBasicParameter.getIncludeColumnFiledNames();
        }


        if (writeBasicParameter.getAccumulator() == null) {
            if (parentAbstractWriteHolder == null) {
                this.accumulator = new DefaultAccumulator();
            } else {
                this.accumulator = parentAbstractWriteHolder.getAccumulator();
            }
        } else {
            this.accumulator = writeBasicParameter.getAccumulator();
        }

        // Set converterMap
        if (parentAbstractWriteHolder == null) {
            setConverterMap(DefaultConverterLoader.loadDefaultWriteConverter());
        } else {
            setConverterMap(new HashMap<>(parentAbstractWriteHolder.getConverterMap()));
        }
        if (writeBasicParameter.getCustomConverterList() != null
                && !writeBasicParameter.getCustomConverterList().isEmpty()) {
            for (Converter converter : writeBasicParameter.getCustomConverterList()) {
                getConverterMap().put(ConverterKeyBuild.buildKey(converter.supportJavaTypeKey()), converter);
            }
        }
    }

    @Override
    public boolean ignore(String fieldName) {
        if (fieldName != null) {
            if (includeColumnFiledNames != null && !includeColumnFiledNames.contains(fieldName)) {
                return true;
            }
            if (excludeColumnFiledNames != null && excludeColumnFiledNames.contains(fieldName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int relativeHeadRowIndex() {
        return getRelativeHeadRowIndex();
    }

    public Boolean getNeedHead() {
        return needHead;
    }

    public void setNeedHead(Boolean needHead) {
        this.needHead = needHead;
    }

    public Integer getRelativeHeadRowIndex() {
        return relativeHeadRowIndex;
    }

    public void setRelativeHeadRowIndex(Integer relativeHeadRowIndex) {
        this.relativeHeadRowIndex = relativeHeadRowIndex;
    }


    public Boolean getUseDefaultStyle() {
        return useDefaultStyle;
    }

    public void setUseDefaultStyle(Boolean useDefaultStyle) {
        this.useDefaultStyle = useDefaultStyle;
    }

    public Boolean getAutomaticMergeHead() {
        return automaticMergeHead;
    }

    public void setAutomaticMergeHead(Boolean automaticMergeHead) {
        this.automaticMergeHead = automaticMergeHead;
    }

    public Collection<String> getExcludeColumnFiledNames() {
        return excludeColumnFiledNames;
    }

    public void setExcludeColumnFiledNames(Collection<String> excludeColumnFiledNames) {
        this.excludeColumnFiledNames = excludeColumnFiledNames;
    }

    public Collection<String> getIncludeColumnFiledNames() {
        return includeColumnFiledNames;
    }

    public void setIncludeColumnFiledNames(Collection<String> includeColumnFiledNames) {
        this.includeColumnFiledNames = includeColumnFiledNames;
    }


    public Accumulator getAccumulator() {
        return accumulator;
    }

    public void setAccumulator(Accumulator accumulator) {
        this.accumulator = accumulator;
    }

}
