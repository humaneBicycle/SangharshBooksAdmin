package com.sangharshAdmin.book;

import com.sangharshAdmin.book.model.FileModel;
import com.sangharshAdmin.book.model.PDFModel;

public interface DirectoryChangeListener {

    void update();
    void onFileModelAdded(FileModel fileModel);
    void onPDFModelAdded(PDFModel pdfModel);

}
