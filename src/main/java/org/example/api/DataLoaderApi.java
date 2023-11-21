package org.example.api;

import lombok.RequiredArgsConstructor;
import org.example.data.FileDataLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/load")
public class DataLoaderApi {
    private final FileDataLoader fileDataLoader;

    @GetMapping("")
    public void loadData() {
        fileDataLoader.loadData();
    }

}
