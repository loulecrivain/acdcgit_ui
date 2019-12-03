package pojo;

/* simple utility class for
 * the representation of working
 * area files in the UI 
 */
public class WaFileStatus {
	private String status;
	private String path;
	
	public WaFileStatus(String status, String path) {
		this.status = status;
		this.path = path;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String toString() {
		return "[" + this.status + "]" + this.path;
	}
}
