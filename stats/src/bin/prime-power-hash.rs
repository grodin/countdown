use color_eyre::eyre;
use stats::hash;
use std::collections::BinaryHeap;
use std::io::{stdin, BufRead};

type Size = u128;

fn main() -> eyre::Result<()> {
    color_eyre::install().expect("Couldn't install panic handler");
    let mut heap: BinaryHeap<(Size, String)> = BinaryHeap::new();

    while let Some(Ok(line)) = stdin().lock().lines().next() {
        debug_assert!(&line.chars().all(|c| c.is_ascii_lowercase()));
        let hash: Size = hash(&line);
        heap.push((hash, line));
    }

    let max = heap.pop().unwrap_or((0, "".to_string()));

    println!("{}: {:x}", max.1, max.0);
    println!("Fits in i64?: {}", max.0 <= i64::MAX as u128);
    println!("Fits in u64?: {}", max.0 <= u64::MAX as u128);

    Ok(())
}
