import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

public class InferenceEngine {
	static HashMap<String, ArrayList<Integer>> positivePredicate1;
	static HashMap<String, ArrayList<Integer>> negativePredicate1;
	static HashMap<String, ArrayList<Integer>> positiveFact;
	static HashMap<String, ArrayList<Integer>> negativeFact;
	static ArrayList<KnowledgeBaseSentence> sentences;
	static HashMap<String, ArrayList<Integer>> positivePredicate2;
	static HashMap<String, ArrayList<Integer>> negativePredicate2;
	static HashMap<String, ArrayList<Integer>> positiveFact2;
	static HashMap<String, ArrayList<Integer>> negativeFact2;
	static ArrayList<KnowledgeBaseSentence> sentences2;
	static HashSet<KnowledgeBaseSentence> seen;
	static HashSet<String> constants;
	static int k = 0;
	static int uo = 0;
	static int vv = 0;
	static boolean o = true;

	public static void main(String[] args) {
		FileReader fr = null;
		int n = 0;
		int m = 0;
		ArrayList<Predicate> queries = new ArrayList<Predicate>();
		sentences = new ArrayList<KnowledgeBaseSentence>();
		positivePredicate1 = new HashMap<String, ArrayList<Integer>>();
		negativePredicate1 = new HashMap<String, ArrayList<Integer>>();
		positiveFact = new HashMap<String, ArrayList<Integer>>();
		negativeFact = new HashMap<String, ArrayList<Integer>>();
		sentences2 = new ArrayList<KnowledgeBaseSentence>();
		positivePredicate2 = new HashMap<String, ArrayList<Integer>>();
		negativePredicate2 = new HashMap<String, ArrayList<Integer>>();
		positiveFact2 = new HashMap<String, ArrayList<Integer>>();
		negativeFact2 = new HashMap<String, ArrayList<Integer>>();
		constants = new HashSet<String>();
		try {
			fr = new FileReader("input.txt");
			BufferedReader br = new BufferedReader(fr);
			n = Integer.parseInt(br.readLine());
			String tytyy;
			for (int i = 0; i < n; i++) {
				tytyy = br.readLine();
				Predicate r = new Predicate(tytyy, "Query");
				queries.add(r);
			}
			m = Integer.parseInt(br.readLine());
			for (int i = 0; i < m; i++) {
				KnowledgeBaseSentence kbs = new KnowledgeBaseSentence(br.readLine());
				sentences.add(kbs);
				sentences2.add(kbs);
				kbs.distribute(sentences.size() - 1);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ee) {

		}
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("output.txt");
			PrintWriter printWriter = new PrintWriter(fileWriter);
			for (Predicate g : queries) {
				sentences = new ArrayList<KnowledgeBaseSentence>();
				positivePredicate1 = new HashMap<String, ArrayList<Integer>>();
				negativePredicate1 = new HashMap<String, ArrayList<Integer>>();
				positiveFact = new HashMap<String, ArrayList<Integer>>();
				negativeFact = new HashMap<String, ArrayList<Integer>>();
				for (String l : positivePredicate2.keySet()) {
					positivePredicate1.put(l, positivePredicate2.get(l));
				}
				for (String l : negativePredicate2.keySet()) {
					negativePredicate1.put(l, negativePredicate2.get(l));
				}
				for (String l : positiveFact2.keySet()) {
					positiveFact.put(l, positiveFact2.get(l));
				}
				for (String l : negativeFact2.keySet()) {
					negativeFact.put(l, negativeFact2.get(l));
				}
				for (KnowledgeBaseSentence l : sentences2) {
					sentences.add(l);
				}
				seen = new HashSet<KnowledgeBaseSentence>();
				KnowledgeBaseSentence j = new KnowledgeBaseSentence();
				g.isPositive = !g.isPositive;
				j.addPredicate(g);
				sentences.add(j);
				j.distribute(sentences.size() - 1, 1);
				if (search(j)) {
					printWriter.println("TRUE");
				} else {
					printWriter.println("FALSE");
				}
			}
			fileWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static void sortPredicates(KnowledgeBaseSentence query) {
		ArrayList<Predicate> r = new ArrayList<Predicate>();
		for (Predicate f : query.predicates) {
			if (!positiveFact.containsKey(f.name) && !negativeFact.containsKey(f.name)) {
				r.add(f);
			}
		}
		for (Predicate g : r) {
			query.predicates.remove(g);

		}
		for (Predicate g : r) {
			query.predicates.add(g);

		}
	}

	static String sortSentence(KnowledgeBaseSentence kb1) {
		ArrayList<Predicate> r = kb1.predicates;
		Collections.sort(r);
		String g = "";
		for (Predicate t : r) {
			if (t.isPositive) {
				g += "@";
			} else {
				g += "&";
			}
			g += t.name;
			for (String y : t.arguements) {
				g += y;
			}
		}
		return g;
	}

	static String sortSentence(KnowledgeBaseSentence kb1, int ioi) {
		ArrayList<Predicate> r = kb1.predicates;
		Collections.sort(r);
		String g = "";
		for (Predicate t : r) {
			g += t.name;
			for (String y : t.arguements) {
				g += y;
			}
		}
		return g;
	}

	static ArrayList<String> getNumberOfVariables(KnowledgeBaseSentence kb1) {
		ArrayList<Predicate> t = kb1.predicates;
		ArrayList<String> w = new ArrayList<String>();
		for (Predicate d : t) {
			ArrayList<String> r = d.arguements;
			for (String u : r) {
				if (Character.isLowerCase(u.charAt(0))) {
					if (!w.contains(u)) {
						w.add(u);
					}
				}
			}
		}
		return w;
	}

	static boolean checkEqualFinal(KnowledgeBaseSentence k1, KnowledgeBaseSentence k2) {
		KnowledgeBaseSentence kb1 = new KnowledgeBaseSentence(k1);
		KnowledgeBaseSentence kb2 = new KnowledgeBaseSentence(k2);
		Hashtable<String, String> gy = new Hashtable<>();
		String mm = "m#";
		for (Predicate d : kb1.predicates) {
			ArrayList<String> arguements = d.arguements;
			for (int io = 0; io < arguements.size(); io++) {
				String r = arguements.get(io);
				if (Character.isLowerCase(r.charAt(0))) {
					if (gy.containsKey(r)) {
						arguements.set(io, gy.get(r));
					} else {
						mm += "#";
						gy.put(r, mm);
						arguements.set(io, mm);
					}
				}
			}
		}
		String j1 = sortSentence(kb1);
		ArrayList<String> y = getNumberOfVariables(kb2);
		ArrayList<ArrayList<String>> df = permutate(y);
		for (ArrayList<String> w : df) {
			String[] e = new String[w.size()];
			e = w.toArray(e);
			for (int i = 0; i < w.size(); i++) {
				String j = "m#";
				KnowledgeBaseSentence uu = new KnowledgeBaseSentence(kb2);
				HashMap<String, String> yuu = new HashMap<>();
				for (int er = 0; er < w.size(); er++) {
					yuu.put(e[er], "");
				}
				for (int er = i; er < w.size(); er++) {
					j += "#";
					yuu.put(e[er], j);
				}
				for (int er = 0; er < i; er++) {
					j += "#";
					yuu.put(e[er], j);
				}
				for (Predicate de : uu.predicates) {
					for (int iu = 0; iu < de.numberOfArguements; iu++) {
						if (yuu.containsKey(de.arguements.get(iu))) {
							de.arguements.set(iu, yuu.get(de.arguements.get(iu)));
						}
					}
				}
				String j2 = sortSentence(uu);
				if (j1.equals(j2)) {
					return true;
				}
			}
		}
		KnowledgeBaseSentence uu = new KnowledgeBaseSentence(k2);
		String gt = sortSentence(uu);
		if (j1.equals(gt)) {
			return true;
		}
		return false;
	}

	private static ArrayList<ArrayList<String>> permutate(ArrayList<String> list) {
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

		result.add(new ArrayList<String>());

		for (int i = 0; i < list.size(); i++) {
			ArrayList<ArrayList<String>> current = new ArrayList<ArrayList<String>>();

			for (ArrayList<String> l : result) {
				for (int j = 0; j < l.size() + 1; j++) {
					l.add(j, list.get(i));

					ArrayList<String> temp = new ArrayList<String>(l);
					current.add(temp);

					l.remove(j);
				}
			}

			result = new ArrayList<ArrayList<String>>(current);
		}

		return result;
	}

	static boolean checkEqualFinal(KnowledgeBaseSentence k1, KnowledgeBaseSentence k2, int ioi) {
		KnowledgeBaseSentence kb1 = new KnowledgeBaseSentence(k1);
		KnowledgeBaseSentence kb2 = new KnowledgeBaseSentence(k2);
		String j1 = sortSentence(kb1, 1);
		String j2 = sortSentence(kb2, 1);
		if (j1.equals(j2)) {
			return true;
		}
		return false;
	}

	static boolean search(KnowledgeBaseSentence query) {
		sortPredicates(query);
		for (KnowledgeBaseSentence b : seen) {
			if (checkEqualFinal(query, b)) {
				return false;
			}
		}
		ArrayList<Integer> w = new ArrayList<>();
		for (int io = 0; io < query.predicates.size() && !w.contains(io); io++) {// Predicate
																					// kl:query.predicates){
			Predicate re = query.predicates.get(io);
			KnowledgeBaseSentence b = new KnowledgeBaseSentence();
			b.addPredicate(re);
			for (int iu = 0; iu < query.predicates.size() && iu != io; iu++) {
				KnowledgeBaseSentence l = new KnowledgeBaseSentence();
				l.addPredicate(query.predicates.get(iu));
				if (checkEqualFinal(b, l, 1) && re.isPositive != query.predicates.get(iu).isPositive) {
					w.add(iu);
					return false;
				}
			}
		}
		seen.add(query);
		sentences.add(query);
		int uuu = sentences.size() - 1;
		query.distribute(uuu, 1);
		int bb = 0;
		int cc = 0;
		int ju = 0;
		for (int i = 0; i < query.predicates.size(); i++) {
			bb = 0;
			cc = 0;
			ju = 0;
			Predicate d = query.predicates.get(i);
			boolean y = d.isPositive;
			if (y) {
				ArrayList<Integer> eFact = negativeFact.get(d.name);
				if (eFact != null) {
					for (Integer r : eFact) {
						KnowledgeBaseSentence kbs2 = new KnowledgeBaseSentence(sentences.get(r));
						ArrayList<KnowledgeBaseSentence> kba = resolution2(query, kbs2, d.name);
						bb += kba.size();
						for (KnowledgeBaseSentence kb : kba) {
							if (kb.predicates.size() == 1 && kb.predicates.get(0).name == "_*") {
								ju++;
							} else {
								if (kb.predicates.size() == 0) {
									ju = 0;
									return true;
								} else {
									boolean b = search(kb);
									if (b) {
										ju = 0;
										return true;
									} else {
										ju++;
									}
								}
							}
						}
					}
				}
				ArrayList<Integer> ePredicate = negativePredicate1.get(d.name);
				if (ePredicate != null) {
					for (Integer r : ePredicate) {
						KnowledgeBaseSentence kbs2 = new KnowledgeBaseSentence(sentences.get(r));
						//kbs2 = factor(kbs2);
						ArrayList<KnowledgeBaseSentence> kba = resolution2(query, kbs2, d.name);
						bb += kba.size();
						for (KnowledgeBaseSentence kb : kba) {
							if (kb.predicates.size() == 1 && kb.predicates.get(0).name == "_*") {
								ju++;
							} else {
								if (kb.predicates.size() == 0) {
									ju = 0;
									return true;
								} else {
									boolean b = search(kb);
									if (b) {
										ju = 0;
										return true;
									} else {
										ju++;
									}
								}
							}
						}
					}
				}
			} else {
				ArrayList<Integer> eFact = positiveFact.get(d.name);
				if (eFact != null) {
					for (Integer r : eFact) {
						KnowledgeBaseSentence kbs2 = new KnowledgeBaseSentence(sentences.get(r));
						ArrayList<KnowledgeBaseSentence> kba = resolution2(query, kbs2, d.name);
						bb += kba.size();
						for (KnowledgeBaseSentence kb : kba) {
							if (kb.predicates.size() == 1 && kb.predicates.get(0).name == "_*") {
								ju++;
							} else {
								if (kb.predicates.size() == 0) {
									ju = 0;
									return true;
								} else {
									boolean b = search(kb);
									if (b) {
										ju = 0;
										return true;
									} else {
										ju++;
									}
								}
							}
						}
					}
				}
				ArrayList<Integer> ePredicate = positivePredicate1.get(d.name);
				if (ePredicate != null) {
					for (Integer r : ePredicate) {
						KnowledgeBaseSentence kbs2 = new KnowledgeBaseSentence(sentences.get(r));
						ArrayList<KnowledgeBaseSentence> kba = resolution2(query, kbs2, d.name);
						bb += kba.size();
						for (KnowledgeBaseSentence kb : kba) {
							if (kb.predicates.size() == 1 && kb.predicates.get(0).name == "_*") {
								ju++;
							} else {
								if (kb.predicates.size() == 0) {
									ju = 0;
									return true;
								} else {
									boolean b = search(kb);
									if (b) {
										ju = 0;
										return true;
									} else {
										ju++;
									}
								}
							}
						}
					}
				}
			}
			if (ju == bb) {
				return false;
			}
		}
		return false;
	}

	static ArrayList<KnowledgeBaseSentence> resolution2(KnowledgeBaseSentence kbs1, KnowledgeBaseSentence kbs2,
			String name) {
		ArrayList<KnowledgeBaseSentence> kba = new ArrayList<KnowledgeBaseSentence>();
		Predicate a1 = null;
		Predicate a2 = null;
		KnowledgeBaseSentence kbs3 = new KnowledgeBaseSentence();
		KnowledgeBaseSentence q = new KnowledgeBaseSentence(kbs1);
		KnowledgeBaseSentence f = new KnowledgeBaseSentence(kbs2);
		for (int i = 0; i < q.predicates.size(); i++) {
			Predicate fp = q.predicates.get(i);
			for (int j = 0; j < fp.numberOfArguements; j++) {
				if (Character.isLowerCase(fp.arguements.get(j).charAt(0))) {
					fp.arguements.set(j, fp.arguements.get(j) + "*");
				}
			}
		}
		for (int i = 0; i < f.predicates.size(); i++) {
			Predicate fp = f.predicates.get(i);
			for (int j = 0; j < fp.numberOfArguements; j++) {
				if (Character.isLowerCase(fp.arguements.get(j).charAt(0))) {
					fp.arguements.set(j, fp.arguements.get(j) + "-");
				}
			}
		}
		for (int ii = 0; ii < q.predicates.size(); ii++) {
			if (q.predicates.get(ii).name.equals(name)) {
				kbs3 = new KnowledgeBaseSentence();
				a1 = q.predicates.get(ii);
				int ij = 0;
				for (ij = 0; ij < f.predicates.size(); ij++) {
					if (f.predicates.get(ij).name.equals(name) && f.predicates.get(ij).isPositive != a1.isPositive) {
						a2 = f.predicates.get(ij);
						Predicate a3 = unifyPredicate(a1, a2);
						if (!a3.name.equals("_*")) {
							Hashtable<String, String> first = new Hashtable<String, String>();
							for (int i = 0; i < a1.numberOfArguements; i++) {
								first.put(a1.arguements.get(i), new String(a3.arguements.get(i)));
							}
							Hashtable<String, String> second = new Hashtable<String, String>();
							for (int i = 0; i < a2.numberOfArguements; i++) {
								second.put(a2.arguements.get(i), new String(a3.arguements.get(i)));
							}
							{
								for (int i = 0; i < q.predicates.size(); i++) {
									Predicate g = q.predicates.get(i);
									for (int j = 0; j < g.numberOfArguements; j++) {
										if (first.containsKey(g.arguements.get(j))) {
											g.arguements.set(j, first.get(g.arguements.get(j)));
										}
									}
								}
								for (int i = 0; i < f.predicates.size(); i++) {
									Predicate g = f.predicates.get(i);
									for (int j = 0; j < g.numberOfArguements; j++) {
										if (second.containsKey(g.arguements.get(j))) {
											g.arguements.set(j, second.get(g.arguements.get(j)));
										}
									}
								}
								for (int i = 0; i < q.predicates.size(); i++) {
									Predicate v = q.predicates.get(i);
									if (i != ii) {
										kbs3.addPredicate(v);
									}
								}
								for (int i = 0; i < f.predicates.size(); i++) {
									Predicate v = f.predicates.get(i);
									if (i != ij) {
										kbs3.addPredicate(v);
									}
								}
								HashSet<String> predicates1 = new HashSet<>();
								KnowledgeBaseSentence kk = new KnowledgeBaseSentence();
								for (Predicate o : kbs3.predicates) {
									KnowledgeBaseSentence l = new KnowledgeBaseSentence();
									l.addPredicate(o);
									boolean mn = true;
									if (predicates1.add(o.getStringFromPredicate())) {
										Predicate ji = new Predicate(o);
										kk.addPredicate(ji);
									}
								}
								kk = factor(kk);
								kbs3 = new KnowledgeBaseSentence(kk);
							}
						} else {
							Predicate ji = new Predicate(a3);
							kbs3.predicates.add(ji);
						}
						kba.add(new KnowledgeBaseSentence(kbs3));
						kbs3 = new KnowledgeBaseSentence();
					}
				}
			}
		}
		return kba;
	}

	static KnowledgeBaseSentence factor(KnowledgeBaseSentence k) {
		KnowledgeBaseSentence j = new KnowledgeBaseSentence(k);
		KnowledgeBaseSentence kk = new KnowledgeBaseSentence();
		for (Predicate o : j.predicates) {
			KnowledgeBaseSentence l = new KnowledgeBaseSentence();
			l.addPredicate(o);
			boolean mn = true;
			Predicate g = null;
			for (Predicate ty : kk.predicates) {
				KnowledgeBaseSentence er = new KnowledgeBaseSentence();
				er.addPredicate(ty);
				g = new Predicate(ty);
				Predicate aa1 = unifyPredicate2(o, g);
				if (!aa1.name.equals("_*")) {// checkEqualFinal(l, er)) {
					mn = false;
					break;
				}
			}

			if (mn) {
				kk.addPredicate(o);
			} else {
				Predicate aa = unifyPredicate2(o, g);
				Hashtable<String, String> first = new Hashtable<String, String>();
				for (int i = 0; i < o.numberOfArguements; i++) {
					first.put(o.arguements.get(i), new String(aa.arguements.get(i)));
				}
				for (int i = 0; i < g.numberOfArguements; i++) {
					first.put(g.arguements.get(i), new String(aa.arguements.get(i)));
				}
				for (int i = 0; i < j.predicates.size(); i++) {
					Predicate g1 = j.predicates.get(i);
					for (int j1 = 0; j1 < g1.numberOfArguements; j1++) {
						if (first.containsKey(g1.arguements.get(j1))) {
							g1.arguements.set(j1, first.get(g1.arguements.get(j1)));
						}
					}
				}
			}
		}
		return kk;
	}

	static Predicate unifyPredicate2(Predicate a1, Predicate a2) {
		if (a1 == null || a2 == null) {
			return new Predicate();
		}
		Predicate c1 = new Predicate(a1);
		Predicate c2 = new Predicate(a2);
		Predicate w = new Predicate(a1);
		if (!c1.name.equals(c2.name)) {
			return new Predicate();
		} else {
			if (a1.isPositive == a2.isPositive) {
				for (int i = 0; i < c1.numberOfArguements; i++) {
					String b1 = c1.arguements.get(i);
					String b2 = c2.arguements.get(i);
					if (Character.isUpperCase(b1.charAt(0)) && Character.isUpperCase(b2.charAt(0))) {
						if (!b1.equals(b2)) {
							return new Predicate();
						}
					} else if (Character.isLowerCase(b1.charAt(0)) && Character.isUpperCase(b2.charAt(0))) {
						for (int j = 0; j < w.arguements.size(); j++) {
							if (w.arguements.get(j).equals(b1)) {
								w.arguements.set(j, b2);
								c1.arguements.set(j, b2);
							}
						}
					} else if (Character.isUpperCase(b1.charAt(0)) && Character.isLowerCase(b2.charAt(0))) {
						for (int j = 0; j < c2.numberOfArguements; j++) {
							if (c2.arguements.get(j).equals(b2)) {
								c2.arguements.set(j, b1);
							}
						}
					} else if (Character.isLowerCase(b1.charAt(0)) && Character.isLowerCase(b2.charAt(0))) {
						for (int j = 0; j < c1.numberOfArguements; j++) {
							if (c2.arguements.get(j).equals(b2)) {
								c2.arguements.set(j, b1);
							}
						}
					}
				}
			} else {
				return new Predicate();
			}
			return c2;
		}
	}

	static Predicate unifyPredicate(Predicate a1, Predicate a2) {
		if (a1 == null || a2 == null) {
			return new Predicate();
		}
		Predicate c1 = new Predicate(a1);
		Predicate c2 = new Predicate(a2);
		Predicate w = new Predicate(a1);
		if (!c1.name.equals(c2.name)) {
			return new Predicate();
		} else {
			if (a1.isPositive != a2.isPositive) {
				for (int i = 0; i < c1.numberOfArguements; i++) {
					String b1 = c1.arguements.get(i);
					String b2 = c2.arguements.get(i);
					if (Character.isUpperCase(b1.charAt(0)) && Character.isUpperCase(b2.charAt(0))) {
						if (!b1.equals(b2)) {
							return new Predicate();
						}
					} else if (Character.isLowerCase(b1.charAt(0)) && Character.isUpperCase(b2.charAt(0))) {
						for (int j = 0; j < w.arguements.size(); j++) {
							if (w.arguements.get(j).equals(b1)) {
								w.arguements.set(j, b2);
								c1.arguements.set(j, b2);
							}
						}
					} else if (Character.isUpperCase(b1.charAt(0)) && Character.isLowerCase(b2.charAt(0))) {
						for (int j = 0; j < c2.numberOfArguements; j++) {
							if (c2.arguements.get(j).equals(b2)) {
								c2.arguements.set(j, b1);
							}
						}
					} else if (Character.isLowerCase(b1.charAt(0)) && Character.isLowerCase(b2.charAt(0))) {
						for (int j = 0; j < c1.numberOfArguements; j++) {
							if (c2.arguements.get(j).equals(b2)) {
								c2.arguements.set(j, b1);
							}
						}
					}
				}
			} else {
				return new Predicate();
			}
			return c2;
		}
	}

	static class KnowledgeBaseSentence {
		ArrayList<Predicate> predicates;

		public KnowledgeBaseSentence() {
			predicates = new ArrayList<Predicate>();
		}

		public KnowledgeBaseSentence(KnowledgeBaseSentence kbs) {
			predicates = new ArrayList<Predicate>();
			for (int i = 0; i < kbs.predicates.size(); i++) {
				predicates.add(new Predicate(kbs.predicates.get(i)));
			}
		}

		public KnowledgeBaseSentence(String g) {
			predicates = new ArrayList<Predicate>();
			String[] l = g.split("\\|");
			for (int i = 0; i < l.length; i++) {
				Predicate t = new Predicate(l[i].trim());
				predicates.add(t);
			}
		}

		public void addPredicate(Predicate l) {
			predicates.add(l);
		}

		public void printKnowledgeBaseSentence() {
			for (int i = 0; i < predicates.size(); i++) {
				predicates.get(i).printPredicate();
			}
		}

		public void distribute(int u, int uu) {
			if (predicates.size() == 1) {
				Predicate t = predicates.get(0);
				if (t.isFact()) {
					if (t.isPositive) {
						ArrayList<Integer> ii = positiveFact.get(t.name);
						ArrayList<Integer> ii2 = new ArrayList<Integer>();
						if (ii == null) {
							ii = new ArrayList<Integer>();
						} else {
							for (Integer er : ii) {
								ii2.add(new Integer(er));
							}
						}
						ii2.add(u);
						positiveFact.put(t.name, ii2);
					} else {
						ArrayList<Integer> ii = negativeFact.get(t.name);
						ArrayList<Integer> ii2 = new ArrayList<Integer>();
						if (ii == null) {
							ii = new ArrayList<Integer>();
						} else {
							for (Integer er : ii) {
								ii2.add(new Integer(er));
							}
						}
						ii2.add(u);
						negativeFact.put(t.name, ii2);
					}
				} else {
					for (int i = 0; i < predicates.size(); i++) {
						Predicate t1 = predicates.get(i);
						if (t1.numberOfArguements > uo) {
							uo = t1.numberOfArguements;
						}
						if (t1.isPositive) {
							ArrayList<Integer> ii = positivePredicate1.get(t1.name);
							ArrayList<Integer> ii2 = new ArrayList<Integer>();
							if (ii == null) {
								ii = new ArrayList<Integer>();
							} else {
								for (Integer er : ii) {
									ii2.add(new Integer(er));
								}
							}
							ii2.add(u);
							positivePredicate1.put(t1.name, ii2);
						} else {
							ArrayList<Integer> ii = negativePredicate1.get(t1.name);
							ArrayList<Integer> ii2 = new ArrayList<Integer>();
							if (ii == null) {
								ii = new ArrayList<Integer>();
							} else {
								for (Integer er : ii) {
									ii2.add(new Integer(er));
								}
							}
							ii2.add(u);
							negativePredicate1.put(t1.name, ii2);
						}
					}
				}
			} else {
				for (int i = 0; i < predicates.size(); i++) {
					Predicate t = predicates.get(i);
					if (t.numberOfArguements > uo) {
						uo = t.numberOfArguements;
					}
					if (t.isPositive) {
						ArrayList<Integer> ii = positivePredicate1.get(t.name);
						ArrayList<Integer> ii2 = new ArrayList<Integer>();
						if (ii == null) {
							ii = new ArrayList<Integer>();
						} else {
							for (Integer er : ii) {
								ii2.add(new Integer(er));
							}
						}
						ii2.add(u);
						positivePredicate1.put(t.name, ii2);
					} else {
						ArrayList<Integer> ii = negativePredicate1.get(t.name);
						ArrayList<Integer> ii2 = new ArrayList<Integer>();
						if (ii == null) {
							ii = new ArrayList<Integer>();
						} else {
							for (Integer er : ii) {
								ii2.add(new Integer(er));
							}
						}
						ii2.add(u);
						negativePredicate1.put(t.name, ii2);
					}
				}
			}
		}

		public void distribute(int u) {
			if (predicates.size() == 1) {
				Predicate t = predicates.get(0);
				if (t.isFact()) {
					if (t.isPositive) {
						ArrayList<Integer> ii = positiveFact.get(t.name);
						if (ii == null) {
							ii = new ArrayList<Integer>();
						}
						ii.add(u);
						positiveFact.put(t.name, ii);
						positiveFact2.put(t.name, ii);
					} else {
						ArrayList<Integer> ii = negativeFact.get(t.name);
						if (ii == null) {
							ii = new ArrayList<Integer>();
						}
						ii.add(u);
						negativeFact.put(t.name, ii);
						negativeFact2.put(t.name, ii);
					}
				} else {
					for (int i = 0; i < predicates.size(); i++) {
						Predicate t1 = predicates.get(i);
						if (t1.numberOfArguements > uo) {
							uo = t1.numberOfArguements;
						}
						if (t1.isPositive) {
							ArrayList<Integer> ii = positivePredicate1.get(t1.name);
							if (ii == null) {
								ii = new ArrayList<Integer>();
							}
							ii.add(u);
							positivePredicate1.put(t1.name, ii);
							positivePredicate2.put(t1.name, ii);
						} else {
							ArrayList<Integer> ii = negativePredicate1.get(t1.name);
							if (ii == null) {
								ii = new ArrayList<Integer>();
							}
							ii.add(u);
							negativePredicate1.put(t1.name, ii);
							negativePredicate2.put(t1.name, ii);
						}
					}
				}
			} else {
				for (int i = 0; i < predicates.size(); i++) {
					Predicate t = predicates.get(i);
					if (t.numberOfArguements > uo) {
						uo = t.numberOfArguements;
					}
					if (t.isPositive) {
						ArrayList<Integer> ii = positivePredicate1.get(t.name);
						if (ii == null) {
							ii = new ArrayList<Integer>();
						}
						ii.add(u);
						positivePredicate1.put(t.name, ii);
						positivePredicate2.put(t.name, ii);
					} else {
						ArrayList<Integer> ii = negativePredicate1.get(t.name);
						if (ii == null) {
							ii = new ArrayList<Integer>();
						}
						ii.add(u);
						negativePredicate1.put(t.name, ii);
						negativePredicate2.put(t.name, ii);
					}
				}
			}
		}
	}

	static class Predicate implements Comparable<Predicate> {
		String name;
		int numberOfArguements;
		ArrayList<String> arguements;
		boolean isPositive;

		Predicate() {
			arguements = new ArrayList<String>();
			name = "_*";
		}

		Predicate(Predicate r) {
			name = new String(r.name);
			numberOfArguements = r.numberOfArguements;
			arguements = new ArrayList<String>();
			isPositive = r.isPositive;
			for (int i = 0; i < numberOfArguements; i++) {
				if (Character.isUpperCase(r.arguements.get(i).charAt(0))) {
					constants.add(r.arguements.get(i));
				}
				arguements.add(new String(r.arguements.get(i)));
			}
		}

		Predicate(String q) {
			arguements = new ArrayList<String>();
			name = "";
			int i = 0;
			int nn = 0;
			if (q.charAt(0) == '~') {
				isPositive = false;
				for (i = 1; i < q.length(); i++) {
					if (q.charAt(i) != '(') {
						name += q.charAt(i);
					} else {
						break;
					}
				}
				i++;
				while (i < q.length() && q.charAt(i) != ')') {
					String t = "";
					while (i < q.length() && q.charAt(i) != ',') {
						if (q.charAt(i) == ')') {
							break;
						}
						t += q.charAt(i);
						i++;
					}
					arguements.add(t);
					i++;
				}
				numberOfArguements = arguements.size();
			} else {
				isPositive = true;
				for (i = 0; i < q.length(); i++) {
					if (q.charAt(i) != '(') {
						name += q.charAt(i);
					} else {
						break;
					}
				}
				i++;
				while (i < q.length() && q.charAt(i) != ')') {
					String t = "";
					while (i < q.length() && q.charAt(i) != ',') {
						if (q.charAt(i) == ')') {
							break;
						}
						t += q.charAt(i);
						i++;
					}
					arguements.add(t);
					i++;
				}
				numberOfArguements = arguements.size();
			}
			for (String m : arguements) {
				if (Character.isUpperCase(m.charAt(0))) {
					constants.add(m);
				}
			}
		}

		Predicate(String q, String query) {
			arguements = new ArrayList<String>();
			name = "";
			int i = 0;
			int nn = 0;
			if (q.charAt(0) == '~') {
				isPositive = false;
				for (i = 1; i < q.length(); i++) {
					if (q.charAt(i) != '(') {
						name += q.charAt(i);
					} else {
						break;
					}
				}
				i++;
				while (i < q.length() && q.charAt(i) != ')') {
					String t = "";
					while (i < q.length() && q.charAt(i) != ',') {
						if (q.charAt(i) == ')') {
							break;
						}
						t += q.charAt(i);
						i++;
					}
					arguements.add(t);
					i++;
				}
				numberOfArguements = arguements.size();
			} else {
				isPositive = true;
				for (i = 0; i < q.length(); i++) {
					if (q.charAt(i) != '(') {
						name += q.charAt(i);
					} else {
						break;
					}
				}
				i++;
				while (i < q.length() && q.charAt(i) != ')') {
					String t = "";
					while (i < q.length() && q.charAt(i) != ',') {
						if (q.charAt(i) == ')') {
							break;
						}
						t += q.charAt(i);
						i++;
					}
					arguements.add(t);
					i++;
				}
				numberOfArguements = arguements.size();
			}
		}

		String getStringFromPredicate() {
			String y = this.name;
			for (String j : this.arguements) {
				y += j;
			}
			return y;
		}

		boolean isFact() {
			boolean a = true;
			for (int i = 0; i < arguements.size(); i++) {
				if (!Character.isUpperCase((arguements.get(i)).charAt(0))) {
					a = false;
					break;
				}
			}
			if (a) {
				return true;
			}
			return true;
		}

		static boolean equalPredicate(Predicate q1, Predicate q2) {
			if (!q1.name.equals(q2.name)) {
				return false;
			} else {
				for (int i = 0; i < q1.arguements.size(); i++) {
					if (!q1.arguements.get(i).equals(q2.arguements.get(i))) {
						return false;
					}
				}
			}
			return true;
		}

		void printPredicate() {
			System.out.println("name :-" + name + " " + isPositive);
			if (arguements.size() > 0) {
				for (int i = 0; i < arguements.size(); i++) {
					System.out.println(arguements.get(i));
				}
			}
		}

		@Override
		public int compareTo(Predicate o) {
			// TODO Auto-generated method stub
			String t = o.name;
			if (this.name.equals(t)) {
				ArrayList<String> y = o.arguements;
				for (int i = 0; i < this.numberOfArguements; i++) {
					if (!y.get(i).equals(this.arguements.get(i))) {
						return this.arguements.get(i).compareTo(y.get(i));
					}
				}
			} else {
				return this.name.compareTo(t);
			}
			return 0;
		}
	}
}
