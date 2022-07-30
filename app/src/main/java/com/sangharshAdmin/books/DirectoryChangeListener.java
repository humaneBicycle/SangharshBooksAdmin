package com.sangharshAdmin.books;

import com.sangharshAdmin.books.model.FileModel;
import com.sangharshAdmin.books.model.PDFModel;

public interface DirectoryChangeListener {

    void update();
    void onFileModelAdded(FileModel fileModel);
    void onPDFModelAdded(PDFModel pdfModel);

}
