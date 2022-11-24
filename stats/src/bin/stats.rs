use color_eyre::eyre;
use itertools::Itertools;
use std::collections::HashMap;
use std::io::{stdin, BufRead};
use std::iter::Iterator;

fn main() -> eyre::Result<()> {
    color_eyre::install().expect("Couldn't install panic handler");
    let alphabet = alphabet();
    let mut histogram = init_histogram(&alphabet);

    while let Some(line) = stdin().lock().lines().next() {
        let line: String = line?.chars().map(|c| c.to_ascii_lowercase()).collect();
        debug_assert!(line.is_ascii());

        for (count, c) in line.chars().sorted().dedup_with_count() {
            histogram.entry(c).and_modify(|(old_count, word)| {
                if &count > old_count {
                    *old_count = count;
                    *word = line.clone();
                }
            });
        }
    }

    for (c, (count, word)) in histogram
        .iter()
        .sorted_by(|(c1, (count1, _)), (c2, (count2, _))| count2.cmp(count1).then(c1.cmp(c2)))
    {
        println!("{}: {}, {}", c, count, word);
    }

    Ok(())
}

fn init_histogram(alphabet: &[char]) -> HashMap<char, (usize, String)> {
    let mut histogram: HashMap<char, (usize, String)> = HashMap::with_capacity(26);
    for &c in alphabet {
        histogram.insert(c, (0, "".to_string()));
    }
    histogram
}

fn alphabet() -> Vec<char> {
    ('a'..'z').collect::<Vec<char>>()
}

#[cfg(test)]
mod test {
    use super::*;
}
