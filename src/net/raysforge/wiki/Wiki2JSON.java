package net.raysforge.wiki;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.raysforge.easyswing.EasySwing;
import net.raysforge.easyswing.EasyTextArea;

public class Wiki2JSON implements ActionListener {

	private EasySwing es;
	private EasyTextArea eta;

	Pattern wikiLink = Pattern.compile("\\[\\[([^|]*)\\|*(.*)\\]\\].*");

	public Wiki2JSON() {
		es = new EasySwing("Wiki2JSON", 800, 600);
		eta = new EasyTextArea();
		es.add(eta);
		es.addToolBarItem("Convert", "convert", this);
		es.show();
	}

	public static void main(String[] args) {

		new Wiki2JSON();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = eta.getText();

		Map<String, String> values = new HashMap<>();

		String[] lines = text.split("\\n");
		for (String line : lines) {
			String[] split = line.split("=");
			if (split.length == 1)
				continue;
			String key = split[0].substring(1).trim();
			String value = split[1].trim();

			Matcher matcher = wikiLink.matcher(value);
			if (matcher.matches()) {
				String g1 = matcher.group(1);
				String g2 = matcher.group(2);
				values.put(key, g2 == null || g2.length() == 0 ? g1 : g2);
			} else {
				values.put(key, value);
			}

		}

		//String keys[] = { "name", "author", "isbn", "country", "language", "release_date", "translator", "pages", "oclc" };
		String keys[] = { "name", "author", "isbn", "country", "language", "published", "pages"};

		StringBuffer sb = new StringBuffer();
		sb.append("{\n");
		for (String key : keys) {
			if(values.get(key)==null)
				sb.append("  " + key + ": null,\n");
			else if(values.get(key).matches("\\d+"))
				sb.append("  " + key + ": " + values.get(key) + ",\n");
			else
				sb.append("  " + key + ": \"" + values.get(key) + "\",\n");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("}\n");
		eta.setText(sb.toString());
	}

}
