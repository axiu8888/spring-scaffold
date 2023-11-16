package com.benefitj.system.controller;

import com.benefitj.core.IOUtils;
import com.benefitj.core.IdUtils;
import com.benefitj.core.file.IUserFileManager;
import com.benefitj.scaffold.file.SystemFileManager;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.ServletUtils;
import com.benefitj.spring.aop.web.AopWebPointCut;
import com.benefitj.system.vo.FileItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文件操作
 */
@Api(tags = {"文件操作"}, description = "上传/下载 文件")
@AopWebPointCut
@RestController
@RequestMapping("/files")
public class FileController {

  @Autowired
  private SystemFileManager systemFileManager;

  @ApiOperation("文件上传")
  @PostMapping
  public HttpResult<?> upload(HttpServletRequest request,
                              @ApiParam("文件路径") MultipartFile[] files,
                              @ApiParam("文件数组") String path) throws IOException {
    if (files == null || files.length == 0) {
      return HttpResult.failure("上传文件不能为空!");
    }
    IUserFileManager ufm = systemFileManager.currentUser();
    File dir = ufm.getDirectory(StringUtils.isNotBlank(path) ? path : "/", true);
    for (MultipartFile file : files) {
      String filename = file.getOriginalFilename();
      filename = StringUtils.isNotBlank(filename) ? filename : IdUtils.nextId(8) + "__" + file.getName();
      ServletUtils.upload(request, file, new File(dir, filename));
    }
    return HttpResult.success("上传成功!");
  }

  @ApiOperation("文件下载")
  @GetMapping
  public void download(HttpServletRequest request,
                       HttpServletResponse response,
                       @ApiParam("文件路径") String path,
                       @ApiParam("文件名称") String filename) throws IOException {
    IUserFileManager ufm = systemFileManager.currentUser();
    File file = ufm.getFile(path, filename);
    if (!file.exists()) {
      response.encodeRedirectURL("/error/404.html");
      return;
    }
    ServletUtils.download(request, response, file, filename);
  }

  @ApiOperation("文件列表")
  @GetMapping("/list")
  public HttpResult<List<FileItem>> list(@ApiParam("文件路径") String path, @ApiParam("多层级") Boolean multiLevel) {
    IUserFileManager ufm = systemFileManager.currentUser();
    File dir = StringUtils.isNotBlank(path) ? ufm.getDirectory(path, false) : ufm.getUserRoot();
    if (dir.exists()) {
      File[] files = dir.listFiles();
      if (files != null) {
        return HttpResult.success(Arrays.stream(files)
            .flatMap(f -> flatDirMap(f, multiLevel))
            .map(file -> FileItem.of(file, ufm.getUserRootPath()))
            .collect(Collectors.toList()));
      }
    }
    return HttpResult.success(Collections.emptyList());
  }

  private Stream<File> flatDirMap(File file, Boolean multiLevel) {
    if (Boolean.TRUE.equals(multiLevel) && file.isDirectory()) {
      File[] files = file.listFiles();
      if (files != null && files.length > 0) {
        return Stream.concat(Stream.of(file), Stream.of(files).flatMap(f -> flatDirMap(f, multiLevel)));
      }
    }
    return Stream.of(file);
  }

  @ApiOperation("文件删除")
  @DeleteMapping
  public HttpResult<?> delete(@ApiParam("文件路径") String path, @ApiParam("文件") String[] files) {
    IUserFileManager ufm = systemFileManager.currentUser();
    File dir = ufm.getDirectory(StringUtils.isNotBlank(path) ? path : "/", true);
    if (files != null && files.length > 0) {
      Arrays.stream(files)
          .filter(StringUtils::isNotBlank)
          .forEach(name -> IOUtils.deleteFile(new File(dir, name)));
    }
    return HttpResult.success();
  }

}
