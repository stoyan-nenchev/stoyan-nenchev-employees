package com.example.employees_csv_app.csv;

import java.io.InputStream;
import java.util.List;

public interface CSVParser<T> {
    List<T> parse(InputStream inputStream);
}
