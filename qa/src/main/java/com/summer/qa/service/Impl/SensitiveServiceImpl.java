package com.summer.qa.service.Impl;

import com.summer.qa.service.SensitiveService;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：lightingSummer
 * @date ：2019/6/5 0005
 * @description：
 */
@Service
public class SensitiveServiceImpl implements SensitiveService, InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(SensitiveServiceImpl.class);

  private static final String DEFAULT_REPLACEMENT = "***";

  private TrieNode root = new TrieNode();

  /**
   * public static void main(String[] args) { SensitiveServiceImpl ss = new SensitiveServiceImpl();
   * ss.addWord("敏感词"); String a = "是与非"; String b = "是与非敏！！感词是与非敏***感//词是与非";
   * System.out.println(ss.filter(a)); System.out.println(ss.filter(b)); }
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    root = new TrieNode();
    try {
      InputStream is =
          Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
      InputStreamReader read = new InputStreamReader(is);
      BufferedReader bufferedReader = new BufferedReader(read);
      String lineTxt;
      while ((lineTxt = bufferedReader.readLine()) != null) {
        lineTxt = lineTxt.trim();
        addWord(lineTxt);
      }
      read.close();
    } catch (Exception e) {
      logger.error("读取敏感词文件失败" + e.getMessage());
    }
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/5 0005
   * @description: 过滤敏感字符+html过滤
   */
  @Override
  public String filter(String text) {
    if (StringUtils.isBlank(text)) {
      return text;
    }
    text = HtmlUtils.htmlEscape(text);
    String replacement = DEFAULT_REPLACEMENT;
    StringBuilder sb = new StringBuilder();
    TrieNode tempNode = root;

    // 可以退回去的字符 position正在比较的字符
    int begin = 0;
    int position = 0;

    while (position < text.length()) {
      char c = text.charAt(position);
      // 判断是否是特殊字符
      if (isSymbol(c)) {
        if (tempNode == root) {
          sb.append(c);
          begin++;
        }
        position++;
        continue;
      }
      // 子树有敏感字符
      TrieNode node = tempNode.getSubNode(c);
      // 如果node不为空,如果到了敏感词末尾，加上***;
      if (node != null) {
        if (node.isEnd()) {
          sb.append(replacement);
          position++;
          begin = position;
          tempNode = root;
        } else {
          // 如果没到末尾，tempNode变为node，继续比较
          position++;
          tempNode = node;
        }
      } else {
        // 如果node为空，则begin加上，从begin下一个开始比较;
        sb.append(text.charAt(begin));
        begin++;
        position = begin;
        tempNode = root;
      }
    }
    // 把最后的加上
    sb.append(text.substring(begin));
    return sb.toString();
  }

  /**
   * @author: lightingSummer
   * @date: 2019/6/5 0005
   * @description: 增加敏感词
   */
  private void addWord(String lineTxt) {
    TrieNode temp = root;
    for (int i = 0; i < lineTxt.length(); i++) {
      char c = lineTxt.charAt(i);
      if (isSymbol(c)) {
        continue;
      }
      TrieNode node = temp.getSubNode(c);
      if (node == null) {
        node = new TrieNode();
        temp.addSubNode(c, node);
      }
      temp = node;
      if (i == lineTxt.length() - 1) {
        temp.setEnd(true);
      }
    }
  }

  /** 判断是否是一个符号 */
  private boolean isSymbol(char c) {
    int ic = (int) c;
    // 0x2E80-0x9FFF 东亚文字范围
    return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
  }

  /**
   * @author : lightingSummer
   * @date : 2019/6/5 0005
   * @description : 敏感词前缀树
   */
  private class TrieNode {
    /** 是否敏感词结尾 */
    private boolean end = false;
    /** 当前节点子节点 */
    private Map<Character, TrieNode> subNode = new HashMap<>();
    /**
     * @author: lightingSummer
     * @date: 2019/6/5 0005
     * @description: 增加子节点
     */
    public void addSubNode(Character key, TrieNode value) {
      subNode.put(key, value);
    }
    /**
     * @author: lightingSummer
     * @date: 2019/6/5 0005
     * @description: 获得子节点
     */
    public TrieNode getSubNode(Character c) {
      return subNode.get(c);
    }

    public boolean isEnd() {
      return end;
    }

    public void setEnd(boolean end) {
      this.end = end;
    }
  }
}
