package timesheet.extras;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import timesheet.DTO.DTOProcess;

public class WindowWatcher implements Runnable {
	private Map<String, Integer> time = new HashMap<String, Integer>();

	public void setTime(Map<String, Integer> time) {
		this.time = time;
	}

	public Map<String, Integer> getAdjustedTime() {
		Map<String, Integer> result = new LinkedHashMap<>();
		Stream<Map.Entry<String, Integer>> st = time.entrySet().stream();

		st.sorted(Map.Entry.comparingByValue()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

		return result;
	}

	private List<String> getWindowTitles() {
		ArrayList<DTOProcess> process = new ArrayList<DTOProcess>();
		try {
			Process p = Runtime.getRuntime().exec("tasklist.exe /v /FO CSV");
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String str = in.readLine();// Title
			if (str == null)
				throw new IllegalAccessError("tasklist.exe is bad");
			str = in.readLine(); // First line
			while (str != null) {
				String[] split = str.split("\",\"");
				DTOProcess proc = new DTOProcess();
				int i = 0;
				proc.setImageName(clean(split[i++]));
				proc.setPID(Integer.valueOf(clean(split[i++])));
				proc.setSessionName(clean(split[i++]));
				proc.setSessionNum(clean(split[i++]));
				proc.setMemUsage(clean(split[i++]));
				proc.setStatus(clean(split[i++]));
				proc.setUsername(clean(split[i++]));
				proc.setCpuTime(clean(split[i++]));
				proc.setWindowTitle(clean(split[i++]));
				process.add(proc);
				str = in.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Set<String> list = new HashSet<>();
		for (DTOProcess dtoProcess : process) {
			if (!"unknown".equalsIgnoreCase(dtoProcess.getUsername())
					&& !"running".equalsIgnoreCase(dtoProcess.getUsername())) {
				list.add(dtoProcess.getWindowTitle());
			}
		}
		return new ArrayList<>(list);
	}

	private static String clean(String str) {
		return str.replace("\"", "");
	}

	@Override
	public void run() {
		while (true) {
			long start = new Date().getTime();
			List<String> windowTitles = getWindowTitles();
			for (String string : windowTitles) {
				if (time.containsKey(string)) {
					Integer i = time.get(string);
					i++;
					time.put(string, i);
				} else {
					time.put(string, 1);
				}
			}
			long end = new Date().getTime();
			try {
				long diff = end - start;
				int wait = 1000;

				long actualWait = (wait - diff) + diff;
				Thread.sleep(actualWait);
				System.out.println(actualWait);

			} catch (InterruptedException e) {
			}
		}
	}
}
