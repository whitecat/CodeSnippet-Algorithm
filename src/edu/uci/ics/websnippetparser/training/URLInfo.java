package edu.uci.ics.websnippetparser.training;

	public class URLInfo {

		private String fileName;
		private String url;

		public URLInfo(String fileName, String url) {
			this.setFileName(fileName);
			this.setUrl(url);
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUrl() {
			return url;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return fileName;
		}

		public String toString() {
			return fileName;
		}
	}