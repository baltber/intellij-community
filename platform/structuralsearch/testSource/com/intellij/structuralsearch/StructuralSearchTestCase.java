// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.structuralsearch;

import com.intellij.codeInsight.daemon.quickFix.LightQuickFixTestCase;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.roots.LanguageLevelProjectExtension;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.pom.java.LanguageLevel;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class StructuralSearchTestCase extends LightQuickFixTestCase {
  protected MatchOptions options;
  protected Matcher testMatcher;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    testMatcher = new Matcher(getProject());
    options = new MatchOptions();
    options.setRecursiveSearch(true);
    LanguageLevelProjectExtension.getInstance(getProject()).setLanguageLevel(LanguageLevel.JDK_1_5);
  }

  @Override
  protected void tearDown() throws Exception {
    testMatcher = null;
    options = null;
    super.tearDown();
  }

  protected int findMatchesCount(String in, String pattern, FileType fileType) {
    return findMatches(in, pattern, fileType).size();
  }

  protected List<MatchResult> findMatches(String in,
                                          String pattern,
                                          FileType patternFileType,
                                          Language patternLanguage,
                                          FileType sourceFileType,
                                          String sourceExtension,
                                          boolean physicalSourceFile) {
    options.clearVariableConstraints();
    options.fillSearchCriteria(pattern);
    options.setFileType(patternFileType);
    options.setDialect(patternLanguage);

    Matcher.validate(getProject(), options);
    return testMatcher.testFindMatches(in, options, true, sourceFileType, sourceExtension, physicalSourceFile);
  }

  protected List<MatchResult> findMatches(String in, String pattern, FileType patternFileType) {
    return findMatches(in, pattern, patternFileType, null, patternFileType, null, false);
  }

  protected int findMatchesCount(String in, String pattern) {
    return findMatchesCount(in, pattern, StdFileTypes.JAVA);
  }

  protected String loadFile(String fileName) throws IOException {
    return FileUtilRt.loadFile(new File(getTestDataPath() + fileName), CharsetToolkit.UTF8, true);
  }
}
