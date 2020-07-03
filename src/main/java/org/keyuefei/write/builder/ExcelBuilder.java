package org.keyuefei.write.builder;

import org.keyuefei.exception.ExcelGenerateException;
import org.keyuefei.write.context.WriteContext;
import org.keyuefei.write.executor.ExcelWriteAddExecutor;
import org.keyuefei.write.executor.ExcelWriteExecutor;
import org.keyuefei.write.metadata.WriteSheet;
import org.keyuefei.write.metadata.WriteWorkbook;

import java.util.List;

public class ExcelBuilder  {

    private WriteContext context;

    private ExcelWriteAddExecutor excelWriteExecutor;


    public ExcelBuilder(WriteWorkbook writeWorkbook) {
        try {
            context = new WriteContext(writeWorkbook);
        } catch (RuntimeException e) {
            finishOnException();
            throw e;
        } catch (Throwable e) {
            finishOnException();
            throw new ExcelGenerateException(e);
        }
    }

    public void addContent(List data, WriteSheet writeSheet) {
        try {
            context.currentSheet(writeSheet);
            if (excelWriteExecutor == null) {
                excelWriteExecutor = new ExcelWriteAddExecutor(context);
            }
            excelWriteExecutor.add(data);
        } catch (RuntimeException e) {
            finishOnException();
            throw e;
        } catch (Throwable e) {
            finishOnException();
            throw new ExcelGenerateException(e);
        }
    }


    private void finishOnException() {
        finish(true);
    }

    public void finish(boolean onException) {
        if (context != null) {
            context.finish(onException);
        }
    }

}
