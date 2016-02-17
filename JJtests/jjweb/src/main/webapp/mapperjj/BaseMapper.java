
public abstract class BaseMapper {

  private static final char SLASH = '/';

  public BaseMapper() { }

  /**
   * Construct a URL from a set of given chunks.  There must be a "/" between each
   * of the various elements.
   *
   * @param chunks Parts of the URL which are to be concatenated.
   * @return reasonable URL
   */
  protected String buildUrl(Object... chunks) {
    StringBuilder buff = new StringBuilder(64);

    for (Object chunk : chunks) {
      String sChunk = String.valueOf(chunk);
      if (buff.length() > 0) {
        char first = (sChunk.length() == 0) ? ' ' : sChunk.charAt(0);
        char last  = buff.charAt(buff.length()-1);
        if (first == SLASH  &&  last == SLASH) {
          sChunk = sChunk.substring(1);
        } else if (first != SLASH  &&  last != SLASH) {
          buff.append(SLASH);
        }
      }
      buff.append(sChunk);
    }

    return buff.toString();
  }
}
